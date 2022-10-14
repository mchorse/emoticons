## Version 1.1.1

**Compatible** with McLib **2.4.1** and optionally with Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

This update was made by Chryfi and MiaoNLI.

* Added global / local translation mode (by Chryfi)
* Fixed NPE in getCurrentPose method due to missing model (by MiaoNLI)
* Fixed sequencer morph animation resetting action (by MiaoNLI)

## Version 1.1

This update was made by MiaoNLI.

**Compatible** with McLib **2.4** and optionally with Blockbuster **2.5** and Metamorph **1.3**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Morph
    * Refresh textures immediately
    * Support generating morph of Metamorph
    * Optimized animation
* GUI
    * Adjusted the width of GuiTransformations
    * Adapted Immersive Editor of Blockbuster
* Rendering
    * Compatible with OptiFine shaders
* File Encoding
    * UTF-8 file encoding is used by default

## Version 1.0.2

This patch fixes stretching shadow and weird right hand extrusion thanks to MiaoNLI's discovery (also special thanks to Chryfi for testing the fix) and placeholder morph merging.

**Compatible** with McLib **2.3** and optionally with Blockbuster **2.3** and Metamorph **1.2.7**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Fixed stretching shadow and weird right hand extrusion (thanks to MiaoNLI's discovery)
* Fixed placeholder morph option not being merged correctly when transitioning from one Emoticons morph to another

## Version 1.0.1

This patch fixes a couple of bugs.

**Compatible** with McLib **2.3** and optionally with Blockbuster **2.3** and Metamorph **1.2.7**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Fixed existing poses crash the game when they don't have specific transformation (i.e. new miscellaneous bones)
* Fixed most of the models lacking miscellaneous bones

## Version 1.0

This update adds more emotes, morph emote system for props (requires Metamorph), a couple of tweaks, and handful of bug fixes.

**Compatible** with McLib **2.3** and optionally with Blockbuster **2.3** and Metamorph **1.2.7**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added copy/paste context menu to pose editor (suggested by Tribble)
* Added support for float (not only integer) x value keyframes
* Added Tada!, Smug Dance, Nope and Ragdoll #3 emotes
* Added morph emote system
* Added 6 misc bones for emotes
* Changed Disable non-emote animations option server side and syncable (suggested by SwilaPlasmark)
* Fixed emotes not working through Blockbuster's `sequencer` morph
* Fixed crash with emote action NPE (reported by Kamesuta)
* Fixed wrong matrix transformation order, aka scaling skewing rotation (reported by zoombie)
* Fixed user animation files not being updated instantly (requires log out and log in)
* Fixed armor meshes being overridden in case user provided a custom mesh config (reported by Centryfuga)
* Fixed 3D emoticons morphs having lines on the outer layer (reported by Chryfi)
* Fixed outer layer offsets among all models (for exception of 3D model type) to be accurate to player models (reported by Yancie)
* Fixed UV distortions for Simple+ models (reported by NyaNLI)

## Version 0.7

This update features new Simple+ model type, and a couple of fixes.

**Compatible** with McLib **2.2** and optionally with Blockbuster **2.2** and Metamorph **1.2.5**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added Simple+ model type that is similar to Simple model type, but has these nice sharp corners
* Changed layout of morph to work similarly to Chameleon mod's morph editor
* Fixed NPE crash during camera editor playback synchronization
* Fixed custom emotes not working on dedicated server (reported by N3w_Err0r)

## Version 0.6.2

This update is a small fix for Blockbuster.

**Compatible** with McLib **2.1** and optionally with Blockbuster **2.1** and Metamorph **1.2.3**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Fixed Chinese localization not working (I forgot to add them, thanks to Chunk7)
* Fixed `emoticons` (Emote action) not working in Blockbuster's record morphs (reported by Herr Bergmann)

## Version 0.6.1

A small update that doesn't do much. 

**Compatible** with McLib **2.1** and optionally with Blockbuster **2.1** and Metamorph **1.2.3**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added two ragdoll emotes
* Added support for paused previews (Blockbuster integration)
* Added a special `ragdoll` armature that allows to import baked ragdoll simulated [actions as an emote action](https://www.youtube.com/watch?v=pn4d__NjMjE)
* Added Chinese localization (thanks to Chunk7)
* Changed simple model option into Model type option (which allows choosing of extruded model type beside default and simple model types)

## Version 0.6

This update adds 6 emotes, a couple of neat features like loading of custom emotes, extruded morphs and a placeholder morph option. It fixes minor performance issue with Emoticons morphs and server emote bug.

**Compatible** with McLib **2.0** and optionally with Blockbuster **2.0** and Metamorph **1.2**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

<a href="https://youtu.be/11lAGyM1Fyc"><img src="https://img.youtube.com/vi/11lAGyM1Fyc/0.jpg"></a> 

* Added **6** new emotes: Stick Bug, Am Stuff, Slow Clap, "Hell, yeah!", Paranoid and Scared
* Added a mechanism to load (and reload) custom animations and emotes
* Added fake 3D Emoticons morphs (which display outer layer as extruded bits)
* Added quick access pick skin keybind to Emoticons morph editor (Shift + P, suggested by zoombie)
* Added placeholder morph option to Emoticons morphs (which allows to setup a morph that will be used when **Disable non-emote animations** option is enabled, and when morph doesn't emote, and for first person arm)
* Fixed players not emoting after death (and possibly on SpongeForge dedicated servers)
* Fixed performance with Emoticons' morphs (it was rendeded twice)
* Fixed minor UV inconsistencies with `default` and `slim` models (reported by Chunk7)

## Version 0.5

This update is compatibility update to work correctly with McLib **2.0**, and others.

**Compatible** with McLib **2.0** and optionally with Blockbuster **2.0** and Metamorph **1.2**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added bone picking by Ctrl + clicking on the blue boxes
* Added all mod options to McLib's mod configurations (Ctrl + 0)
* Added player preview option category
* Added empty sound files for easier replacement of sounds for emotes
* Fixed to work with newer versions of McHorse's mods
* Fixed particle morphs from Blockbuster not working correctly from body parts of `emoticons.*` morphs

## Version 0.4.3

This hot patch update fixes stupid transparency issue with model when used with Optifine, because Optifine changes blending function smh.

**Compatible** with McLib **1.0.4** and optionally with Blockbuster **1.6.4** and Metamorph **1.1.10**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Fixed semi-transparent model due to Optifine not cleaning up its OpenGL state

## Version 0.4.2

This update fixes a couple of issues with multiplayer servers ("wallhack" aka F3 skeleton rendering through walls and invisibility potions).

**Compatible** with McLib **1.0.4** and optionally with Blockbuster **1.6.4** and Metamorph **1.1.10**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added a check box "Fixate movement" for specific bone in pose editor
* Added new Nazzy's animations (swimming, landing and dying)
* Fixed model getting rendered when player under invisibility effect
* Fixed armature being rendered and acting as a wall hack 
* Fixed animated poses not working with poseless morph
* Fixed vehicle causing rotation issues (helicopter spinning)

## Version 0.4.1

This tiny patch update makes Emoticons work with Blockbuster 1.6.4.

**Compatible** with McLib **1.0.4** and optionally with Blockbuster **1.6.4** and Metamorph **1.1.10**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Fixed a crash with Blockbuster 1.6.4

## Version 0.4

This small update that adds 9 different emote and fixes a couple of bugs.

<a href="https://youtu.be/M76ugB8vtaU"><img src="https://img.youtube.com/vi/M76ugB8vtaU/0.jpg"></a> 

**Compatible** with McLib **1.0.4** and optionally with Blockbuster **1.6.2** and Metamorph **1.1.10**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added 9 new emotes: **Bongo Cat**, **Breathtaking**, **Disgusted**, **Exhausted**, **Punch!**, **Slap**, **Sneeze**, **Threatening** and **Woah**
* Fixed/improve player animation by fixing bezier function to work correctly
* Fixed sitting oriented toward one direction with chairs from other mods

## Version 0.3.2

This small patch update provides new emote animations made by [Nazzy](https://www.youtube.com/channel/UCQ2L7O1KDK7ze75dSe1C-yg) and Moris, beside that it also fixes one mod compatibility issue with [PlayerRevive](https://www.curseforge.com/minecraft/mc-mods/playerrevive) mod.

**Compatible** with McLib **1.0.3** and optionally with Blockbuster **1.6** and Metamorph **1.1.8**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added new non-emote animations (which are made by **Nazzy** and **Moris**)
* Decresed the file size by doing some internal rewriting and file management
* Fixed player not lying on the ground when using PlayerRevive mod by **CreativeMD**

## Version 0.3.1

This patch update fixes few issues with emote synchronization.

**Compatible** with McLib **1.0.3** and optionally with Blockbuster **1.6** and Metamorph **1.1.8**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added custom payload channel `Emoticons` (send `<player_username>` to establish connection, and `<player_username> <emote_id>` to send the emote playing)
* Fixed proper name tag rendering (not just the name but also server side formatting, if there is any)
* Fixed emote server synchronization not working after player dies or goes to another dimension
* Fixed /emote commmand not working on the server.
* Fixed Blockbuster emote action not working with Blockbuster's body actors

## Version 0.3

<a href="https://youtu.be/OmruW-fz7ro"><img src="https://img.youtube.com/vi/OmruW-fz7ro/0.jpg"></a> 

This long update took long quite long to get finished, and I don't think I actually got done anything finished, but here we go.

**Compatible** with McLib **1.0.3** and optionally with Blockbuster **1.6** and Metamorph **1.1.8**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added new emotes: Get Funky, Free Flow, Shimmer, Twerk and Clubbing
* Added new mod dependency **McLib**
* Added simplified models
* Added config option to disable sound event registering
* Added 2 more emote keybinds
* Added Emoticons morph editor to support full configuration of morphs
* Added Emoticons morph animated poses
* Changed different model shading method
* Fixed crash with some strict graphics card drivers
* Fixed crash with dedicated server and morphs

## Version 0.2.1

Third update. This quick patch provides few important tweaks. This update removed the F1 watermark, fixes dying animation not showing up during emote, adds empty sound events for dances, adds an option in emote configuration menu to disable non-emote animations, decreased the file size of the jar, and finally added extra information on the crash screen (so it would be easy to understand what's going on with access key).

## Version 0.2

Includes support for vanilla armor, a tweak to emote key binds (which allows changing to another emote without stopping an emote), and also 10 new emotes: Fist, Gangnam Style, Pointing, Gopak aka Squat Kick (suggested by Andruxioid), Salute, Skibidi, Shrug, T-pose, Take the L and Rock-paper-scissors.

## Version 0.1

Initial release of Emoticons mod (which was rebranded from Fortcraft before release). This inlcludes 4 emote keybinds, 28 emotes and substitute animations for player model.