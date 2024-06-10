package akatrox.discord.sync;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.dv8tion.jda.api.OnlineStatus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class AkatroxDiscordSync extends JavaPlugin implements Listener {

    private JDA jda;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, String> pendingVerification = new ConcurrentHashMap<>();
    private final Map<String, String> playerDiscordIds = new HashMap<>();
    private final Map<String, Boolean> playerVerificationStatus = new HashMap<>();

    private static final String PREFIX_RAW = "#ff0000AkatroxDiscordSync &8» ";
    private static final String PREFIX = ColorUtil.color(PREFIX_RAW);

    private String language;
    private FileConfiguration langConfig;

    @Override
    public void onLoad() {
        String pluginName = getDescription().getName();
        String expectedJarName = getConfig().getString("jar-name", "AkatroxDiscordSync.jar");
        String actualJarName = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        if (!pluginName.equals("AkatroxDiscordSync") || !actualJarName.equals(expectedJarName)) {
            System.out.println("--------------------------------------------------");
            System.out.println(" ");
            System.out.println(ColorUtil.translateAnsiColorCodes("&cChanging the plugin name or JAR file name is prohibited and unethical. "));
            System.out.println(ColorUtil.translateAnsiColorCodes("&cPlease change the plugin name to AkatroxDiscordSync and the JAR file name to " + expectedJarName));
            System.out.println(" ");
            System.out.println("--------------------------------------------------");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onEnable() {
        String pluginName = getDescription().getName();
        String expectedJarName = getConfig().getString("jar-name", "AkatroxDiscordSync.jar");
        String actualJarName = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        if (!pluginName.equals("AkatroxDiscordSync") || !actualJarName.equals(expectedJarName)) {
            System.out.println("--------------------------------------------------");
            System.out.println(" ");
            System.out.println(ColorUtil.translateAnsiColorCodes("&cChanging the plugin name or JAR file name is prohibited and unethical. "));
            System.out.println(ColorUtil.translateAnsiColorCodes("&cPlease change the plugin name to AkatroxDiscordSync and the JAR file name to " + expectedJarName));
            System.out.println(" ");
            System.out.println("--------------------------------------------------");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("akatroxdiscordsync").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        reloadConfig();
        language = getConfig().getString("language", "tr");

        loadPlayerData();
        saveDefaultLanguageFile("lang_tr.yml");
        saveDefaultLanguageFile("lang_en.yml");
        reloadLanguageFile();
        startDiscordBot();

        System.out.println("--------------------------------------------------");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(ColorUtil.translateAnsiColorCodes("&aAkatrox Discord Synchronization plugin is enabled"));
        System.out.println(ColorUtil.translateAnsiColorCodes("&aThank you for using the plugin"));
        System.out.println(" ");
        System.out.println(ColorUtil.translateAnsiColorCodes("&aDeveloped by benakatrox"));
        System.out.println(ColorUtil.translateAnsiColorCodes("&awww.akatrox.com.tr - discord.gg/akatrox"));
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("--------------------------------------------------");
    }

    private void startDiscordBot() {
        String token = getConfig().getString("discord.token");
        String statusMessage = getConfig().getString("discord.status-message", "by benakatrox");
        String status = getConfig().getString("discord.status", "online"); // status ayarını çekiyoruz

        try {
            JDABuilder builder = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .addEventListeners(new SlashCommandListener());

            switch (status.toLowerCase()) {
                case "idle":
                    builder.setStatus(OnlineStatus.IDLE);
                    break;
                case "dnd":
                    builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
                    break;
                case "invisible":
                    builder.setStatus(OnlineStatus.INVISIBLE);
                    break;
                case "offline":
                    builder.setStatus(OnlineStatus.OFFLINE);
                    break;
                default:
                    builder.setStatus(OnlineStatus.ONLINE);
                    break;
            }

            jda = builder.build();
            jda.getPresence().setActivity(Activity.playing(statusMessage));
            jda.awaitReady();

            jda.updateCommands().addCommands(
                    Commands.slash("verify", "Verify a player")
                            .addOptions(new OptionData(OptionType.STRING, "playername", "The name of the player to verify", true))
            ).queue();

            System.out.println("--------------------------------------------------");
            System.out.println(" ");
            System.out.println(ColorUtil.translateAnsiColorCodes("&aDiscord bot connection successful, welcome!"));
            System.out.println(" ");
            System.out.println("--------------------------------------------------");

        } catch (Exception e) {

            System.out.println("--------------------------------------------------");
            System.out.println(" ");
            System.out.println(ColorUtil.translateAnsiColorCodes("Exception occurred while starting the Discord bot;"));
            System.out.println("&c" + e.toString());
            System.out.println(" ");
            System.out.println("--------------------------------------------------");

            e.printStackTrace();
        }
    }


    @Override
    public void onDisable() {
        savePlayerData();
        if (jda != null) {
            jda.shutdown();
        }
        System.out.println("--------------------------------------------------");
        System.out.println(" ");
        System.out.println(ColorUtil.translateAnsiColorCodes("&cAkatrox Discord Synchronization plugin is disabled"));
        System.out.println(" ");
        System.out.println("--------------------------------------------------");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String discordUserId = playerDiscordIds.get(player.getName());
        if (discordUserId != null) {
            discordUpdate(player, discordUserId, getConfig().getString("discord.guild-id"));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.color(PREFIX + getMessage("only_game_command")));
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("akatroxdiscordsync")) {
            if (args.length == 0) {
                sender.sendMessage(ColorUtil.color(PREFIX + "Usage: /akatroxdiscordsync <verify|reload|info>"));
                return true;
            }

            String subCommand = args[0];

            switch (subCommand.toLowerCase()) {
                case "verify":
                    if (!player.hasPermission("akatroxdiscordsync.user")) {
                        player.sendMessage(ColorUtil.color(PREFIX + getMessage("no_permission")));
                        return true;
                    }

                    if (args.length != 2) {
                        sender.sendMessage(ColorUtil.color(PREFIX + getMessage("verify_command_usage")));
                        return true;
                    }

                    String code = args[1];
                    if (validateCode(player, code)) {
                        sender.sendMessage(ColorUtil.color(PREFIX + getMessage("verification_success")));
                        String discordUserId = pendingVerification.remove(player.getName());
                        if (discordUserId != null) {
                            discordUpdate(player, discordUserId, getConfig().getString("discord.guild-id"));
                            storePlayerData(player.getName(), discordUserId, true);
                        }
                    } else {
                        sender.sendMessage(ColorUtil.color(PREFIX + getMessage("invalid_code")));
                    }
                    break;

                case "reload":
                    if (!player.isOp()) {
                        player.sendMessage(ColorUtil.color(PREFIX + getMessage("no_permission")));
                        return true;
                    }
                    reloadConfig();
                    reloadLanguageFile();
                    sender.sendMessage(ColorUtil.color(PREFIX + getMessage("config_reloaded")));
                    break;

                case "info":
                    if (!player.hasPermission("akatroxdiscordsync.user")) {
                        player.sendMessage(ColorUtil.color(PREFIX + getMessage("no_permission")));
                        return true;
                    }

                    String discordUserId = playerDiscordIds.get(player.getName());
                    if (discordUserId != null) {
                        Member member = jda.getGuildById(getConfig().getString("discord.guild-id")).retrieveMemberById(discordUserId).complete();
                        if (member != null) {
                            sender.sendMessage(ColorUtil.color(PREFIX + "Discord Username: " + member.getUser().getName()));
                        } else {
                            sender.sendMessage(ColorUtil.color(PREFIX + getMessage("discord_user_not_found")));
                        }
                    } else {
                        sender.sendMessage(ColorUtil.color(PREFIX + getMessage("no_discord_linked")));
                    }
                    break;

                default:
                    sender.sendMessage(ColorUtil.color(PREFIX + "Usage: /akatroxdiscordsync <verify|reload|info>"));
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("akatroxdiscordsync")) {
            if (args.length == 1) {
                List<String> subCommands = Arrays.asList("verify", "reload", "info");
                return subCommands.stream()
                        .filter(subCommand -> subCommand.startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    public void discordUpdate(Player player, String discordUserId, String guildId) {
        Guild guild = jda.getGuildById(guildId);
        if (guild == null) {
            System.out.println(ColorUtil.translateAnsiColorCodes("&cGuild not found: " + guildId));
            return;
        }

        ConfigurationSection ranksSection = getConfig().getConfigurationSection("ranks");
        if (ranksSection == null) {
            System.out.println(ColorUtil.translateAnsiColorCodes("&cRanks section not found in config"));
            return;
        }

        Member member = guild.retrieveMemberById(discordUserId).complete();
        if (member == null) {
            System.out.println(ColorUtil.translateAnsiColorCodes("&cMember not found: " + discordUserId));
            return;
        }

        String defaultRoleId = getConfig().getString("default_sync_perm_id");
        if (defaultRoleId != null) {
            Role defaultRole = guild.getRoleById(defaultRoleId);
            if (defaultRole != null) {
                if (!playerVerificationStatus.getOrDefault(player.getName(), false)) {
                    assignDefaultGifts(player);
                }
                guild.addRoleToMember(member, defaultRole).queue(
                        success -> System.out.println(ColorUtil.translateAnsiColorCodes("&aDefault role " + defaultRole.getName() + " assigned to " + player.getName())),
                        error -> System.out.println(ColorUtil.translateAnsiColorCodes("&cFailed to assign default role: " + error.getMessage())));
            } else {
                System.out.println(ColorUtil.translateAnsiColorCodes("&cDefault role not found: " + defaultRoleId));
            }
        } else {
            System.out.println(ColorUtil.translateAnsiColorCodes("&cDefault role ID not found in config."));
        }

        for (String group : ranksSection.getKeys(false)) {
            String permission = "akatroxdiscordsync." + group;
            ConfigurationSection groupSection = ranksSection.getConfigurationSection(group);
            if (groupSection != null) {
                String roleId = groupSection.getString("id");
                if (roleId != null) {
                    Role role = guild.getRoleById(roleId);
                    if (role != null) {
                        if (hasPermission(player, permission)) {
                            System.out.println(ColorUtil.translateAnsiColorCodes("&aAssigning role " + role.getName() + " to player " + player.getName()));
                            guild.addRoleToMember(member, role).queue(
                                    success -> System.out.println(ColorUtil.translateAnsiColorCodes("&aRole " + role.getName() + " assigned to " + player.getName())),
                                    error -> System.out.println(ColorUtil.translateAnsiColorCodes("&cFailed to assign role: " + error.getMessage())));
                        } else {
                            guild.removeRoleFromMember(member, role).queue(
                                    success -> System.out.println(ColorUtil.translateAnsiColorCodes("&aRole " + role.getName() + " removed from " + player.getName())),
                                    error -> System.out.println(ColorUtil.translateAnsiColorCodes("&cFailed to remove role: " + error.getMessage())));
                        }
                    } else {
                        System.out.println(ColorUtil.translateAnsiColorCodes("&cRole not found: " + roleId));
                    }
                } else {
                    System.out.println(ColorUtil.translateAnsiColorCodes("&cRole ID for group " + group + " not found in config."));
                }
            } else {
                System.out.println(ColorUtil.translateAnsiColorCodes("&cNo configuration found for group: " + group));
            }
        }
    }

    private void assignDefaultGifts(Player player) {
        if (playerVerificationStatus.getOrDefault(player.getName(), false)) {
            return;
        }
        System.out.println("Assigning default gifts to player: " + player.getName());
        List<String> commands = getConfig().getStringList("default_gift");
        for (String command : commands) {
            if (command.startsWith("execute console command ")) {
                command = command.replace("execute console command ", "");
            }
            String parsedCommand = command.replace("{player}", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
        }
    }

    public class SlashCommandListener extends ListenerAdapter {
        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equals("verify")) {
                String playerName = event.getOption("playername").getAsString();
                Player player = Bukkit.getPlayer(playerName);
                if (player == null || !player.isOnline()) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(getMessage("embed_title_error"));
                    embed.setDescription(getMessage("embed_description_player_not_online"));
                    embed.setColor(Color.RED);
                    embed.setFooter(getMessage("embed_footer"));
                    event.replyEmbeds(embed.build()).queue();
                    return;
                }

                if (isPlayerInJson(playerName)) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(getMessage("embed_title_error"));
                    embed.setDescription(getMessage("embed_description_already_verified"));
                    embed.setColor(Color.RED);
                    embed.setFooter(getMessage("embed_footer"));
                    event.replyEmbeds(embed.build()).queue();
                    return;
                }

                String code = generateRandomCode();
                setPlayerVerificationCode(playerName, code);
                pendingVerification.put(playerName, event.getUser().getId());

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle(getMessage("embed_title_verification_code"));
                embed.setDescription(getMessage("embed_description_verification_code")
                        .replace("{player_name}", playerName)
                        .replace("{code}", code));
                embed.setColor(Color.GREEN);
                embed.setFooter(getMessage("embed_footer"));

                event.replyEmbeds(embed.build()).queue();
            }
        }

        private boolean isPlayerInJson(String playerName) {
            File file = new File(getDataFolder(), "playerData.json");
            if (!file.exists()) {
                return false;
            }

            try (FileReader reader = new FileReader(file)) {
                JSONArray jsonArray = new JSONArray(new JSONTokener(reader));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String savedPlayerName = jsonObject.getString("playerName");
                    if (savedPlayerName.equals(playerName)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private boolean validateCode(Player player, String code) {
        String storedCode = verificationCodes.get(player.getName());
        if (storedCode != null && storedCode.equals(code)) {
            String discordUserId = pendingVerification.get(player.getName());
            if (discordUserId != null) {
                pendingVerification.remove(player.getName());
                discordUpdate(player, discordUserId, getConfig().getString("discord.guild-id"));
                storePlayerData(player.getName(), discordUserId, true);
                return true;
            }
        }
        return false;
    }

    private boolean hasPermission(Player player, String permission) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            boolean hasPerm = user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
            System.out.println(ColorUtil.translateAnsiColorCodes("&aChecking permission for player " + player.getName() + ": " + permission + " = " + hasPerm));
            return hasPerm;
        } else {
            System.out.println(ColorUtil.translateAnsiColorCodes("&cUser not found in LuckPerms: " + player.getName()));
            return false;
        }
    }

    private void loadPlayerData() {
        File file = new File(getDataFolder(), "playerData.json");
        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            StringBuilder jsonContent = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                jsonContent.append((char) i);
            }

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                String playerName = jsonObject.getString("playerName");
                String discordId = jsonObject.getString("discordId");
                boolean isVerified = jsonObject.getBoolean("isVerified");

                playerDiscordIds.put(playerName, discordId);
                playerVerificationStatus.put(playerName, isVerified);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerData() {
        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<String, String> entry : playerDiscordIds.entrySet()) {
            String playerName = entry.getKey();
            String discordId = entry.getValue();
            boolean isVerified = playerVerificationStatus.getOrDefault(playerName, false);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("playerName", playerName);
            jsonObject.put("discordId", discordId);
            jsonObject.put("isVerified", isVerified);

            jsonArray.put(jsonObject);
        }

        try (FileWriter writer = new FileWriter(new File(getDataFolder(), "playerData.json"))) {
            writer.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storePlayerData(String playerName, String discordId, boolean isNewlyVerified) {
        playerDiscordIds.put(playerName, discordId);
        playerVerificationStatus.put(playerName, true);
        savePlayerData();
        if (isNewlyVerified) {
            assignDefaultGiftsOnce(playerName);
        }
    }

    private void assignDefaultGiftsOnce(String playerName) {
        if (playerVerificationStatus.getOrDefault(playerName, false)) {
            return;
        }
        System.out.println("Assigning default gifts to player: " + playerName);
        List<String> commands = getConfig().getStringList("default_gift");
        for (String command : commands) {
            if (command.startsWith("execute console command ")) {
                command = command.replace("execute console command ", "");
            }
            String parsedCommand = command.replace("{player}", playerName);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private String getPlayerVerificationCode(Player player) {
        return verificationCodes.get(player.getName());
    }

    public void setPlayerVerificationCode(String playerName, String code) {
        verificationCodes.put(playerName, code);
    }

    private String getDiscordUserId(Player player) {
        return pendingVerification.get(player.getName());
    }

    private void saveDefaultLanguageFile(String fileName) {
        File langFile = new File(getDataFolder(), fileName);
        if (!langFile.exists()) {
            saveResource(fileName, false);
        }
    }

    private void reloadLanguageFile() {
        langConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang_" + language + ".yml"));
    }

    private String getMessage(String key) {
        switch (key) {
            case "embed_title_error":
                return language.equals("tr") ? "Hata" : "Error";
            case "embed_description_already_verified":
                return language.equals("tr") ? "Girmiş olduğunuz kullanıcı adı daha önce eşleştirilmiş!" : "The username you entered has already been matched!";
            case "embed_description_player_not_online":
                return language.equals("tr") ? "Belirttiğiniz kullanıcı şuanda oyunda bulunmuyor!" : "The specified player is not currently online!";
            case "embed_footer":
                return "developed by benakatrox ✅ www.akatrox.com.tr - discord.gg/akatrox";
            case "embed_title_verification_code":
                return language.equals("tr") ? "Doğrulama Kodu" : "Verification Code";
            case "embed_description_verification_code":
                return language.equals("tr") ? "Oyuncu adınız: **{player_name}**\nDoğrulama kodunuz: **{code}**" : "Your player name: **{player_name}**\nYour verification code: **{code}**";
            default:
                return langConfig.getString(key, "");
        }
    }
}
