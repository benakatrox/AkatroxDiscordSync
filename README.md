------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
⠀
⠀
⠀
**TURKISH**
⠀
**AkatroxDiscordSync**
⠀
**Eklenti amacı: Discord ve Minecraft sunucunuz arasında senkronizasyon işlemleri gerçekleştirir ve birçok özelliğe sahiptir.**
⠀
**Dikkat! eklenti çalışması için LuckPerms ve PlaceHolderAPI eklentilerine ihtiyaç duyar.**
⠀
⠀
_İşte birkaç özellik_
⠀
- Belirli rütbe gruplarını belirleyip hesaplarını eşleştiren kullanıcıların o grupta olup olmadığını kontrol edin ve Discord sunucunuzda o role sahip olmalarını sağlayın. Örneğin: akatroxdiscordsync.vip iznini VIP grubunuza verin (.vip kısmını yapılandırmada ayarlarsınız) ve Discord rol ID'sinin karşılığını yapılandırmada ayarlayın. Kullanıcı VIP satın alırsa, otomatik olarak Discord'da VIP rolüne sahip olur. Dahası, bu kullanıcı sunucuya girdiğinde eşzamanlı olarak kontrol edilir. VIP süresi dolarsa, rol geri alınır.
⠀
- Standart bir eşleştirme rolü oluşturun ve bu role özel ödüller verin. Hesaplarını eşleştiren ancak herhangi bir grup veya destek sahibi olmayan kullanıcılara yine de Discord adresinizde bir senkronizasyon rolü verebilirsiniz. Dahası, yapılandırmadan hesapları eşleştiren kullanıcıların eşleştirdikten sonra kazanabilecekleri ödülleri varsayılan olarak ayarlayabilirsiniz.
⠀
- PlaceHolderAPI desteği "%akatroxdiscordsync_boost% / %akatroxdiscordsync_sync% / %akatroxdiscordsync_claimboostrewardtiming%"
⠀
- Discord sunucunuzun desteklenip desteklenmediğini kontrol etmek için kullanıcıların desteklediklerinde özel bir haftalık ödül komutu (/adsync claimboost) oyun içinde kullanmalarını sağlayın.
⠀
- Oyuncu raporlama sisteminin bu oyun içi komutu sayesinde, Discord kanalınızda anlık oyun içi şikayetleri takip edebilirsiniz. (/adsync report <player> <reason>)
⠀
- Oyun içi komutları ve oyun içi mesajları anında Discord kanallarınızda kaydedin ve işleyin.
⠀
- Discord'da hesap eşleştirme komutunun çalışacağı kanalı ayarlayın ve tek bir kanalda çalışmasını sağlayın. (Komutun her kanalda çalışmasını ve göz kirliliğine neden olmasını önler)
⠀
- Discord kanalında bir /serverinfo komutu sayesinde oyun hakkında çevrimiçi sayısı çevrimiçi oyuncular ve uptime süresi gibi bilgilere anlık olarak erişmenizi sağlar.
⠀
⠀
**Komutlar**
⠀
_Discord_
⠀
/serverinfo : Bu bir Discord slash komutudur. Amacı oyun hakkında çevrimiçi sayısı çevrimiçi oyuncular ve uptime süresi gibi bilgilere anlık olarak erişmenizi sağlar.
/verify <oyun içi adınız> : Bu bir Discord slash komutudur. Amacı hesabınızı eşlemek için bir kod oluşturursunuz. Bu kodu sonrasında oyunda /adsync verify <kod> şeklinde girip hesabınızı eşlersiniz.
⠀
_Oyun İçi_
⠀
/adsync verify <kod> : Bu oyun içinden Discorddan aldığınız kodu girerek hesabınızı eşlemenize yarar.
/adsync report <oyuncu adı> <rapor sebebi> : Bir oyuncu hakkında şikayetinizi dile getirirsiniz. Discordda ayarlanan kanala ileterek yetkililere hızlıca ulaşabilmenizi sağlar.
/adsync info : Eşlediğiniz hesabın Discord kullanıcı adı bilgisini öğrenirsiniz. (İlerde geliştirilcek daha detaylı olacak)
/adsync reload : Eklentinin tüm Dil ve Config dosyalarını yenilersiniz. (Önemli değişikliklerde direkt sunucuyu baştan başlatmanız önerilir)
⠀
⠀
**Yetkiler**
⠀
akatroxdiscordsync.user : Hesap eşleme ve info gibi temel oyuncu komutlarını kullanabilmenize yarar.
akatroxdiscordsync.report : Oyuncuların bir rapor oluşturabilmesine olanak tanır.
⠀
akatroxdiscordsync.<perm> : Bu configte ayarladığınız bir yetki grubuna Discordda özel rol vermenize olanak sağlar configte aşağıdaki gibi vip yazılan yere ne yazarsanız LuckPerms'tede o gruba o yetkiyi vereceksiniz. Örneğin vip yazmışsanız aşağıdaki gibi
LuckPerms vip grubunuza akatroxdiscordsync.vip yetkisini vereceksiniz.
⠀
ranks:
  vip:
    id: "DISCORD_ROLE_ID"
⠀
⠀
**PlaceHolderAPI Desteği**
⠀
PlaceHolderAPI sayesinde sunucunuzda Hologram & Scoreboard & TAB gibi bir çok yerde oyuncuları bilgilendirebilirsiniz. Bunlar aşağıda yer almaktadır.
⠀
%akatroxdiscordsync_sync% : Bu oyuncunun hesabını eşleyip eşlemediğini gösterir. True & False olarak değer döndürür.
%akatroxdiscordsync_boost% : Bu oyuncunun Discord sunucunuza boost basıp basmadığını gösterir. True & False olarak değer döndürür.
%akatroxdiscordsync_claimboostrewardtiming% : Bu oyuncunuz eğer Discord sunucunuza boost basıp ödülünü aldıysa (/adsync claimboost) tekrar ödülünü almaya kalan zamanı gösterir. (Oyuncular haftada bir kez ödül alır buda onun geri sayımını gösterir)
⠀
⠀
⠀
**Bu ve birçok başka özellikle, eklentimiz her geçen gün gelişmeye devam ediyor. Eklentimizi indirip kullanarak yorum yapmayı ve bize destek olmak için Discord adresimize katılmayı unutmayın! Şimdiden teşekkürler!**
⠀
⠀
⠀

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
⠀
⠀
⠀
**ENGLISH**

**AkatroxDiscordSync**
⠀
**Plugin purpose: It performs synchronization operations between your Discord and Minecraft server and contains many features.**
⠀
**Attention! The plugin requires LuckPerms and PlaceHolderAPI plugins to work.**
⠀
⠀
_Here are a few features_

- Check if users who determine certain rank groups and match their accounts are in that group and ensure they have that role on your Discord server. For example: Give the akatroxdiscordsync.vip permission to your VIP group (you set the .vip part in the config) and set the equivalent Discord role ID in the configuration. If the user purchases VIP, they will automatically have the VIP role in Discord. Moreover, when this user enters the server, it is checked simultaneously. If the VIP period expires, the role is taken back.
⠀
- Create a standard matching role and give specific rewards to that role. You can still set up and give a sync role in your Discord address to users who have synchronized accounts but do not have any groups or boosts. Moreover, you can set the rewards that users who match accounts can earn after matching from the config by default.
⠀
- PlaceHolderAPI support "%akatroxdiscordsync_boost% / %akatroxdiscordsync_sync% / %akatroxdiscordsync_claimboostrewardtiming%"
⠀
- To check whether your Discord server has been boosted or not, allow users to use a special weekly reward command (/adsync claimboost) in-game when they boost.
⠀
- Thanks to this in-game command of the player reporting system, you can track instant in-game complaints via your Discord channel. (/adsync report <player> <reason>)
⠀
- Instantly log and process in-game commands and in-game messages on your Discord channels.
⠀
- Set the channel where the account matching command in Discord will run and ensure that it runs on a single channel. (Prevents the command from running on every channel and causing clutter)
⠀
- A /serverinfo command in Discord allows you to access information about the game such as the number of online players and uptime duration instantly.
⠀
⠀
**Commands**
⠀
_Discord_
⠀
/serverinfo : This is a Discord slash command. It allows you to access information about the game such as the number of online players and uptime duration instantly.
/verify <your in-game name> : This is a Discord slash command. It generates a code to link your account. You then enter this code in the game as /adsync verify <code> to link your account.
⠀
_In-Game_
⠀
/adsync verify <code> : This allows you to link your account by entering the code obtained from Discord.
/adsync report <player name> <report reason> : You express your complaint about a player. It sends it to the designated channel on Discord to reach the authorities quickly.
/adsync info : You learn the Discord username information of the linked account. (Will be more detailed in future updates)
/adsync reload : Reloads all Language and Config files of the plugin. (It is recommended to restart the server directly for major changes)

⠀
**Permissions**
⠀
akatroxdiscordsync.user : Allows you to use basic player commands like account linking and info.
akatroxdiscordsync.report : Allows players to create a report.
⠀
akatroxdiscordsync.<perm> : Allows you to give a special role in Discord to a permission group set in the config. Whatever you write in the place where vip is written below in the config, you will also give that permission to that group in LuckPerms. For example, if you wrote vip, you will give the akatroxdiscordsync.vip permission to your LuckPerms vip group as shown below.
⠀
ranks:
vip:
id: "DISCORD_ROLE_ID"
⠀
⠀
**PlaceHolderAPI Support**
⠀
Thanks to PlaceHolderAPI, you can inform players in many places such as Hologram, Scoreboard & TAB in your server. These are listed below.
⠀
%akatroxdiscordsync_sync% : Shows whether the player has linked their account or not. Returns a value of True & False.
%akatroxdiscordsync_boost% : Shows whether the player has boosted your Discord server or not. Returns a value of True & False.
%akatroxdiscordsync_claimboostrewardtiming% : If your player has boosted your Discord server and claimed their reward (/adsync claimboost), it shows the remaining time to claim the reward again. (Players can claim rewards once a week, this shows the countdown)
⠀
⠀
⠀
**With this and many other features, our plugin continues to improve day by day. Don't forget to support us by downloading and using our plugin, commenting, and joining our Discord address to support us! Thanks in advance!**
⠀
⠀
⠀

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
