**Update 1.1**

**To benefit from the updates, add the changes in the config or delete the config file and let it recreate, and also delete your language files and let it recreate.**

_New Features_

- Added support for PlaceHolderAPI with the variables %akatroxdiscordsync_boost%, %akatroxdiscordsync_sync%, and %akatroxdiscordsync_claimboostrewardtiming%.
- Added the ability to check for boosts on the Discord server and set up weekly rewards for users who boost.
- In-game report submission with the command /report <reason>.
- Commands for account linking can only be executed in a specified channel.
- Ability to set a channel to log in-game chat and commands to a Discord channel.
- Fixed errors in Turkish and English language support.

_The above features can be configured in the config file as follows:_


report-channel-id: "REPORT_CHANNEL_ID" # ID of the Discord channel that will receive the reports
verify-channel-id: "VERIFY_CHANNEL_ID"  # Discord channel ID to which users will match their accounts
message-channel-id: "MESSAGE_CHANNEL_ID" # Discord channel where in-game messages will be sent
command-channel-id: "COMMAND_CHANNEL_ID" # Discord channel to send in-game commands

boost_rewards:
  - "execute console command give {player} minecraft:golden_apple 1"
