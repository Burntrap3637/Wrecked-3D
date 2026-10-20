;//////////////////////////////////////////////////////////////////////////////
;---------------------------- WRECKED: VARIABLES ------------------------------
;//////////////////////////////////////////////////////////////////////////////

;STRUCTURE
Const FPS=30
Global screen,go,gotim
Global foc,subfoc,keytim
Global filein,fileout,saver,loader
Global optPause

;PROGRESS
;universals
Global game,slot,gamFile$="Save01"
Global gamIsland,gamDestination
Global gamJourney,travelX#,travelY#
Global no_missions=16
Global gamSurvivor
;individual progress
Dim gamTime(3)
Dim gamDay(3)
Dim gamSoul(3) ;1=evil, 2=flawed, 3=divine
Dim gamSins(3,20) ;-1=resolved, 1=thief, 2=animal murder, 3=human murder 
Dim gamCrime(3)
Dim gamMission(3)
Dim gamMissionDay(3)
Dim gamResult(3,20)
;1=soldier intro, 2=mystic delay, 3=theft occurred?, 4=started?, 5=ended?
Dim gamCheckpoint(3,10) 
;resource placement
Global gamPlanting=0,gamPlanter=61
Global gamCollisions=0
;narrative
Global promo,promoTim
;default progress
ResetProgressData(0)
;For cyc=1 To 3
 ;ResetProgressData(cyc)
;Next

;OPTIONS
.Options
Global optRes,currRes,optSpeed=1
Global optDisplay=3,optBlood=1,optFX=1
Global optMusic=5,optAtmos=1,optWeather
Global optSeaLevel#=1.5
;deathmatch
Global optIsland,optPlays=8,optCast,optMulti
;keys & buttons
Global keyAttackA=30,keyAttackB=31,keyJump=57
Global keyPickA=44,keyPickB=45,keySleep=29
Global buttAttackA=4,buttAttackB=5,buttJump=6
Global buttPickA=1,buttPickB=2,buttSleep=3

;----------------------- SOUND -------------------------
.Sound
;music
Global chTheme,musicVol#,musicTVol#
Global sTheme=LoadSound("Sound/Theme.wav")
Global sJingle=LoadSound("Sound/Jingle.wav")
;atmospherics
Global sAmbience,chAmbience
Global sAtmos=LoadSound("Sound/Atmosphere.wav")
Global sCoast=LoadSound("Sound/Coast.wav")
Global sRain=LoadSound("Sound/Rain.wav")
;menu effects
Global sMenuSelect=LoadSound("Sound/MenuSelect.wav")
Global sMenuBrowse=LoadSound("Sound/MenuBrowse.wav")
Global sMenuGo=LoadSound("Sound/MenuGo.wav")
Global sMenuBack=LoadSound("Sound/MenuBack.wav")
Global sCamera=LoadSound("Sound/Camera.wav")
Global sTrash=LoadSound("Sound/Trash.wav")
;game effects
Global sStep=LoadSound("Sound/Step.wav")
Global sShuffle=LoadSound("Sound/Shuffle.wav")
Global sSwing=LoadSound("Sound/Swing.wav")
Global sLand=LoadSound("Sound/Land.wav")
Global sFall=LoadSound("Sound/Fall.wav")
Global sHit=LoadSound("Sound/Hit.wav")
Global sEat=LoadSound("Sound/Eat.wav")
Global sDrink=LoadSound("Sound/Drink.wav")
Global sBleed=LoadSound("Sound/Bleed.wav")
Global sCough=LoadSound("Sound/Cough.wav")
Global sSnoring=LoadSound("Sound/Snoring.wav")
Global sDying=LoadSound("Sound/Dying.wav")
Global sSink=LoadSound("Sound/Sink.wav")
;creature sounds
Dim sAttack(5)
For count=1 To 4
 sAttack(count)=LoadSound("Sound/Attack0"+count+".wav")
Next
Dim sPain(5)
For count=1 To 4
 sPain(count)=LoadSound("Sound/Pain0"+count+".wav")
Next
Dim sDeath(5)
For count=1 To 4
 sDeath(count)=LoadSound("Sound/Death0"+count+".wav")
Next
;materials
Global sVines=LoadSound("Sound/Vines.wav")
Global sWood=LoadSound("Sound/Wood.wav")
Global sStone=LoadSound("Sound/Stone.wav")
Global sMeat=LoadSound("Sound/Meat.wav")
Global sWhip=LoadSound("Sound/Meat.wav")
Global sSpear=LoadSound("Sound/Spear.wav")
Global sAxe=LoadSound("Sound/Axe.wav")
Global sGun=LoadSound("Sound/Gun.wav")
Global sBone=LoadSound("Sound/Bone.wav")
;elements
Global chFire
Global sFireLoop=LoadSound("Sound/Fire Loop.wav")
Global sFireIgnite=LoadSound("Sound/Fire Ignite.wav")
Global sFireExpire=LoadSound("Sound/Fire Expire.wav") 
Global sFireSinge=LoadSound("Sound/Fire Expire.wav") 
Global sSplash=LoadSound("Sound/Splash.wav")

;--------------------- GRAPHICS ------------------------
.Images
;variables
Dim font(10)
Dim fontX(10)
Dim gLogo(5)
Dim gHeader(5)
Dim gMeter(5)
Global gTile,tileX#,tileY#
Global gMDickie,gOutro
Global gMenuA,gMenuB
Global gPaused,gCalendar
Global gCamera,gSpeed
Global gMap,gIndicator
Dim gJourney(12,3)
Dim gPreview(3)
;LOADING PROCESS
Function LoadImages()
 ;regular fonts
 font(0)=LoadFont("helvetica",11,1,0,0)
 font(1)=LoadFont("helvetica",15,1,0,0)
 font(2)=LoadFont("helvetica",20,1,0,0)
 font(3)=LoadFont("helvetica",28,1,0,0)
 font(4)=LoadFont("helvetica",36,1,0,0)
 font(5)=LoadFont("helvetica",48,1,0,0)
 ;special fonts
 fontX(1)=LoadFont("EraserDust.ttf",20,0,0,0)
 fontX(2)=LoadFont("EraserDust.ttf",25,0,0,0)
 fontX(3)=LoadFont("EraserDust.ttf",36,0,0,0)
 fontX(4)=LoadFont("EraserDust.ttf",48,0,0,0)
 ;logos
 gTile=LoadImage("Graphics/Tile.JPG")
 MaskImage gTile,255,0,255
 For count=1 To 2
  gLogo(count)=LoadImage("Graphics/Logo0"+count+".png")
  MaskImage gLogo(count),255,0,255
 Next
 gMDickie=LoadImage("Graphics/MDickie.png")
 MaskImage gMDickie,255,0,255
 ;menu boxes
 gMenuA=LoadImage("Graphics/MenuA.bmp")
 MaskImage gMenuA,255,0,255
 gMenuB=LoadImage("Graphics/MenuB.bmp")
 MaskImage gMenuB,255,0,255
 ;game displays
 For count=1 To 3
  gHeader(count)=LoadImage("Graphics/Header0"+count+".png")
  MaskImage gHeader(count),255,0,255
 Next
 For count=0 To 4
  gMeter(count)=LoadImage("Graphics/Meter0"+count+".bmp")
  MaskImage gMeter(count),255,0,255
 Next
 gCalendar=LoadImage("Graphics/Calendar.png")
 MaskImage gCalendar,255,0,255
 gPaused=LoadImage("Graphics/Paused.png")
 MaskImage gPaused,255,0,255
 gSpeed=LoadImage("Graphics/Speed.bmp")
 MaskImage gSpeed,255,0,255
 gCamera=LoadImage("Graphics/Camera.bmp")
 MaskImage gCamera,255,0,255
 ;map diagrams
 gMap=LoadImage("Graphics/Travel/Map.png")
 MaskImage gMap,255,0,255
 gIndicator=LoadImage("Graphics/Travel/Indicator.bmp")
 MaskImage gIndicator,255,0,255
 For cyc=1 To 12
  For count=1 To 3
   gJourney(cyc,count)=LoadImage("Graphics/Travel/Journey"+Dig$(cyc,10)+"-"+Dig$(count,10)+".png")
   MaskImage gJourney(cyc,count),255,0,255
  Next
 Next
 ;slot previews
 For count=1 To 3
  gPreview(count)=LoadImage("Data/Save0"+count+"/Preview.bmp")
  MaskImage gPreview(count),255,0,255
 Next
 ;outro
 gOutro=LoadImage("Graphics/Outro.JPG")
 MaskImage gOutro,255,0,255
End Function

;--------------------- TEXTURES ------------------------
.Textures
;variables
Global tWater,tRain
Dim tTree(5)
Dim tHair(20)
Dim tFace(20)
Dim tBody(20)
Dim tArms(20)
Dim tLegs(20)
Dim tEyes(5)
Dim tMouth(5)
Dim tFaceScar(5)
Dim tBodyScar(5)
;LOADING PROCESS
Function LoadTextures()
 ;world
 For count=1 To 5
  tTree(count)=LoadTexture("World/Sprites/Tree0"+count+".bmp",4)
 Next
 tWater=LoadTexture("World/Sprites/Water.JPG") 
 tRain=LoadTexture("World/Sprites/Rain.bmp",4)
 ;hair costumes
 For count=1 To 4
  tHair(count)=LoadTexture("Characters/Costumes/Hair/Hair"+Dig$(count,10)+".JPG") 
 Next
 ;face costumes
 For count=1 To 13
  tFace(count)=LoadTexture("Characters/Costumes/Faces/Face"+Dig$(count,10)+".JPG") 
 Next
 ;body costumes
 For count=1 To 10
  tBody(count)=LoadTexture("Characters/Costumes/Bodies/Body"+Dig$(count,10)+".JPG") 
 Next
 ;arm costumes
 For count=1 To 9
  tArms(count)=LoadTexture("Characters/Costumes/Arms/Arms"+Dig$(count,10)+".JPG") 
 Next
 ;leg costumes
 For count=1 To 9
  tLegs(count)=LoadTexture("Characters/Costumes/Legs/Legs"+Dig$(count,10)+".JPG") 
 Next
 ;eye expressions
 For count=1 To 3
  tEyes(count)=LoadTexture("Characters/Expressions/Eyes"+Dig$(count,10)+".JPG") 
 Next
 ;mouth expressions
 For count=0 To 5
  tMouth(count)=LoadTexture("Characters/Expressions/Mouth"+Dig$(count,10)+".JPG") 
 Next
 ;facial scarring
 For count=0 To 4
  tFaceScar(count)=LoadTexture("Characters/Scarring/Face"+Dig$(count,10)+".JPG") 
 Next
 ;body scarring
 For count=0 To 4
  tBodyScar(count)=LoadTexture("Characters/Scarring/Body"+Dig$(count,10)+".JPG") 
 Next
End Function

;----------------------- WORLD -------------------------
.World
Global world
Global wNorth#,wSouth#,wEast#,wWest#
Global wWaterNorth#,wWaterSouth#
Global wWaterEast#,wWaterWest#
Global wNorthLead,wSouthLead
Global wEastLead,wWestLead
;CAMERA
Global no_cameras=1,camDefault=3
Global camPivot,dummy
Global camType,camTim
Dim cam(2),camFoc(2)
Dim camX#(2),camY#(2),camZ#(2)
Dim camTX#(2),camTY#(2),camTZ#(2)
Global camSpeedX#,camSpeedY#,camSpeedZ#
Global camDiffX#,camDiffY#,camDiffZ#
Global camAnchor#,camSpeed
;lighting
Global light,lightPivot
Global lightR#=200,lightG#=200,lightB#=200
Global lightTR#,lightTG#,lightTB#
Global ambR#=220,ambG#=220,ambB#=220
Global ambTR#,ambTG#,ambTB#
;atmosphere
Global atmosR#=200,atmosG#=200,atmosB#=200
Global atmosTR#,atmosTG#,atmosTB#
Global skyR#=255,skyG#=255,skyB#=255
Global skyTR#,skyTG#,skyTB#
;weather
Global wRainFlow#,wWaterFlow#
Dim rain(100)

;-------------------- PLAYERS -------------------------
.Players
Global no_plays=60
Dim p(no_plays)
Dim pPivot(no_plays)
Dim pIsland(no_plays)
Dim pX#(no_plays)
Dim pY#(no_plays)
Dim pZ#(no_plays)
Dim pOldX#(no_plays)
Dim pOldY#(no_plays)
Dim pOldZ#(no_plays)
Dim pA#(no_plays)
Dim pGround#(no_plays)
Dim pGravity#(no_plays)
;AI
Dim pAgenda(no_plays)
Dim pTarget(no_plays)
Dim pTX#(no_plays)
Dim pTZ#(no_plays)
Dim pTA#(no_plays)
Dim pNowhere(no_plays)
;triggers
Dim pControl(no_plays)
Dim cUp(no_plays)
Dim cDown(no_plays)
Dim cLeft(no_plays)
Dim cRight(no_plays)
Dim pCharge#(no_plays)
Dim cAttackA(no_plays)
Dim cAttackB(no_plays)
Dim cPickA(no_plays)
Dim cPickB(no_plays)
Dim cJump(no_plays)
;animation
Dim pSeq(no_plays,210)
Dim pAnim(no_plays)
Dim pAnimTim(no_plays)
Dim pOldAnim(no_plays)
Dim pStepTim(no_plays)
Dim pSplashTim(no_plays)
Dim pStagger#(no_plays)
;contruction
Dim pOldProduct(no_plays)
Dim pProduct(no_plays)
Dim pRecipe(no_plays,20)
Dim pWork(no_plays)
;status
Dim pType(no_plays)
Dim pState(no_plays)
Dim pHunger(no_plays)
Dim pThirst(no_plays)
Dim pHealth(no_plays)
Dim pHP(no_plays)
Dim pDT(no_plays)
Dim pSpeed#(no_plays)
Dim pOldSpeed#(no_plays)
Dim pCarryA(no_plays) 
Dim pCarryB(no_plays)
Dim pSting(no_plays)
;appearance
Dim pLimb(no_plays,20)
Dim pScar(no_plays,20)
Dim pOldScar(no_plays,20)
Dim pCostume(no_plays)
Dim pHair(no_plays)
Dim pFace(no_plays)
Dim pBody(no_plays)
Dim pArms(no_plays)
Dim pLegs(no_plays)
Dim pEyes(no_plays)
Dim pOldEyes(no_plays)
Dim pMouth(no_plays)
Dim pSpeaking(no_plays)
;additional
Dim pShadow(no_plays)
;PROPERTIES
.Characters
Dim charName$(30)
Dim charAttack(30)
Dim charPain(30)
Dim charDeath(30)
Dim charWidth(30)
Dim charHeight(30)
Dim charSpeed#(30)
Dim charAnim#(30)
Dim charRange(30)
Dim charPower(30)
Dim charHP(30)
Dim charMeat(30)
;humans (1=player, 2=soldier, 3=mystic, 4=native)
For count=1 To 4
 charName$(count)="Player"
 charAttack(count)=0 : charPain(count)=1 : charDeath(count)=1
 charWidth(count)=8 : charHeight(count)=30 : charSpeed#(count)=2.5 : charAnim#(count)=0
 charRange(count)=4 : charPower(count)=1 : charHP(count)=20 : charMeat(count)=2
Next
;leopard
charName$(10)="Leopard"
charAttack(10)=2 : charPain(10)=2 : charDeath(10)=2
charWidth(10)=13 : charHeight(10)=14 : charSpeed#(10)=2.6 : charAnim#(10)=0
charRange(10)=6 : charPower(10)=3 : charHP(10)=15 : charMeat(10)=2
;wolf
charName$(11)="Wolf"
charAttack(11)=3 : charPain(11)=3 : charDeath(11)=3
charWidth(11)=11 : charHeight(11)=12 : charSpeed#(11)=1.8 : charAnim#(11)=1.0
charRange(11)=5 : charPower(11)=2 : charHP(11)=12 : charMeat(11)=2
;deer
charName$(12)="Deer"
charAttack(12)=4 : charPain(12)=4 : charDeath(12)=4
charWidth(12)=12 : charHeight(12)=19 : charSpeed#(12)=1.4 : charAnim#(12)=1.0
charRange(12)=0 : charPower(12)=0 : charHP(12)=12 : charMeat(12)=2
;rabbit
charName$(13)="Rabbit"
charAttack(13)=0 : charPain(13)=0 : charDeath(13)=4
charWidth(13)=6 : charHeight(13)=8 : charSpeed#(13)=1.0 : charAnim#(13)=2.0
charRange(13)=0 : charPower(13)=0 : charHP(13)=8 : charMeat(13)=1
;fish
charName$(20)="Fish"
charAttack(20)=0 : charPain(20)=0 : charDeath(20)=0
charWidth(20)=4 : charHeight(20)=12 : charSpeed#(20)=2.0 : charAnim#(20)=0
charRange(20)=0 : charPower(20)=0 : charHP(20)=5 : charMeat(20)=1
;PLACE IN WORLD
For cyc=1 To no_plays
 randy=Rnd(1,4)
 If randy=1 Then pIsland(cyc)=1
 If randy=2 Then pIsland(cyc)=2
 If randy=3 Then pIsland(cyc)=3
 If randy=>4 Then pIsland(cyc)=5
 pIsland(cyc)=gamIsland
 randy=Rnd(0,8)
 If randy=<1 Then pType(cyc)=1
 If randy=2 Then pType(cyc)=10
 If randy=3 Then pType(cyc)=11
 If randy=4 Then pType(cyc)=12
 If randy=5 Or randy=6 Then pType(cyc)=13
 If randy=7 Or randy=8 Then pType(cyc)=20
Next
pIsland(1)=gamIsland : pType(1)=1
pIsland(2)=gamIsland : pType(2)=1

;---------------------- RESOURCES ---------------------------
.Resources
;identifiers
Global no_resources=150
Dim res(no_resources)
Dim resIsland(no_resources)
Dim resType(no_resources)
Dim resState(no_resources) ;0=inactive, 1=active
Dim resX#(no_resources)
Dim resY#(no_resources)
Dim resZ#(no_resources)
Dim resA#(no_resources)
Dim resHP(no_resources)
;animation
Dim resAnim(no_resources)
Dim resAnimTim(no_resources)
;PROPERTIES
Dim resName$(10)
Dim resSound(10)
Dim resWidth(10)
Dim resHeight(10)
Dim resStrength(10)
Dim resVines(10)
Dim resWood(10)
Dim resStone(10)
Dim resFruit(10)
;bare tree
resName$(1)="BareTree" : resSound(1)=sVines
resWidth(1)=9 : resHeight(1)=28 : resStrength(1)=40
resVines(1)=0 : resWood(1)=2 : resStone(1)=0 : resFruit(1)=0
;palm tree
resName$(2)="PalmTree" : resSound(2)=sVines
resWidth(2)=7 : resHeight(2)=30 : resStrength(2)=30
resVines(2)=1 : resWood(2)=2 : resStone(2)=0 : resFruit(2)=0
;regular tree 
resName$(3)="Tree" : resSound(3)=sVines
resWidth(3)=9 : resHeight(3)=28 : resStrength(3)=40
resVines(3)=1 : resWood(3)=2 : resStone(3)=0 : resFruit(3)=0
;fruit tree
resName$(4)="Tree" : resSound(4)=sVines
resWidth(4)=9 : resHeight(4)=28 : resStrength(4)=40
resVines(4)=1 : resWood(4)=1 : resStone(4)=0 : resFruit(4)=2
;bush
resName$(5)="Bush" : resSound(5)=sVines
resWidth(5)=10 : resHeight(5)=8 : resStrength(5)=20
resVines(5)=2 : resWood(5)=0 : resStone(5)=0 : resFruit(5)=0
;fruit bush
resName$(6)="Bush" : resSound(6)=sVines
resWidth(6)=10 : resHeight(6)=8 : resStrength(6)=20
resVines(6)=1 : resWood(6)=0 : resStone(6)=0 : resFruit(6)=2
;plant
resName$(7)="Plant" : resSound(7)=sVines
resWidth(7)=10 : resHeight(7)=10 : resStrength(7)=20
resVines(7)=2 : resWood(7)=0 : resStone(7)=0 : resFruit(7)=1
;rock
resName$(8)="Rock" : resSound(8)=sStone
resWidth(8)=15 : resHeight(8)=12 : resStrength(8)=50
resVines(8)=0 : resWood(8)=0 : resStone(8)=2 : resFruit(8)=0

;---------------------- ITEMS ---------------------------
.Items
;identifiers
Global no_items=375,iRange1,iRange2
Dim i(no_items)
Dim iIsland(no_items)
Dim iType(no_items) ;1-9=resources,10+=weapons/tools
Dim iState(no_items) ;0=inactive, 1=active
Dim iX#(no_items)
Dim iY#(no_items)
Dim iZ#(no_items)
Dim iA#(no_items)
Dim iOldX#(no_items)
Dim iOldY#(no_items)
Dim iOldZ#(no_items)
Dim iBuilding(no_items) ;being worked by a human?
Dim iTrapTim(no_items)
Dim iFlaming(no_items)
;physics
Dim iBouncer#(no_items)
Dim iGravity#(no_items)
Dim iFlight#(no_items)
Dim iSting(no_items,no_plays)
;PROPERTIES
Dim iName$(20)
Dim iSound(20)
Dim iAnim(20) ;9=punch, 11=swing
Dim iWeight#(20)
Dim iRange(20)
Dim iDamage(20)
Dim iFlammable(20)
;*Resources
;vines
iName$(1)="Vines" : iSound(1)=sVines : iAnim(1)=9
iWeight#(1)=0.14 : iRange(1)=4 : iDamage(1)=1
iFlammable(1)=1000
;wood
iName$(2)="Wood" : iSound(2)=sWood : iAnim(2)=9
iWeight#(2)=0.16 : iRange(2)=4 : iDamage(2)=2
iFlammable(2)=2000
;stone
iName$(3)="Stone" : iSound(3)=sStone : iAnim(3)=9
iWeight#(3)=0.2 : iRange(3)=4 : iDamage(3)=3
iFlammable(3)=0
;meat
iName$(4)="Meat" : iSound(4)=sMeat : iAnim(4)=9
iWeight#(4)=0.15 : iRange(4)=4 : iDamage(4)=1
iFlammable(4)=1000
;fruit
iName$(5)="Fruit" : iSound(5)=sMeat : iAnim(5)=9
iWeight#(5)=0.15 : iRange(5)=4 : iDamage(5)=1
iFlammable(5)=500
;*Weapons
;whip
iName$(10)="Whip" : iSound(10)=sWhip : iAnim(10)=11
iWeight#(10)=0.12 : iRange(10)=6 : iDamage(10)=1
iFlammable(10)=500
;stick
iName$(11)="Stick" : iSound(11)=sWood : iAnim(11)=11
iWeight#(11)=0.14 : iRange(11)=7 : iDamage(11)=2
iFlammable(11)=1000
;rock
iName$(12)="Rock" : iSound(12)=sStone : iAnim(12)=9
iWeight#(12)=0.18 : iRange(12)=4 : iDamage(12)=3
iFlammable(12)=0
;hammer
iName$(13)="Hammer" : iSound(13)=sStone : iAnim(13)=11
iWeight#(13)=0.2 : iRange(13)=4 : iDamage(13)=4
iFlammable(13)=500
;spear
iName$(14)="Spear" : iSound(14)=sSpear : iAnim(14)=11
iWeight#(14)=0.17 : iRange(14)=7 : iDamage(14)=4
iFlammable(14)=1000
;axe
iName$(15)="Axe" : iSound(15)=sAxe : iAnim(15)=11
iWeight#(15)=0.22 : iRange(15)=5 : iDamage(15)=5
iFlammable(15)=500
;gun!
iName$(16)="Gun" : iSound(16)=sGun : iAnim(16)=11
iWeight#(16)=0.25 : iRange(16)=7 : iDamage(16)=4
iFlammable(16)=0
;artefact
iName$(17)="Artefact" : iSound(17)=sAxe : iAnim(17)=9
iWeight#(17)=0.25 : iRange(17)=4 : iDamage(17)=1
iFlammable(17)=0
;bone
iName$(18)="Bone" : iSound(18)=sBone : iAnim(18)=11
iWeight#(18)=0.15 : iRange(18)=5 : iDamage(18)=1
iFlammable(18)=0
;trap
iName$(19)="Trap" : iSound(19)=sVines : iAnim(19)=0
iWeight#(19)=0.25 : iRange(19)=5 : iDamage(19)=5
iFlammable(19)=1000
;oar
iName$(20)="Oar" : iSound(20)=sWood : iAnim(20)=11
iWeight#(20)=0.16 : iRange(20)=7 : iDamage(20)=3
iFlammable(20)=1000

;--------------- PARTICLE EFFECTS ------------------------
.Particles
Global no_particles=400
Dim part(1000)
Dim partType(1000)
Dim partX#(1000)
Dim partY#(1000)
Dim partZ#(1000)
Dim partA#(1000)
Dim partGravity#(1000)
Dim partFlight#(1000)
Dim partSize#(1000)
Dim partAlpha#(1000)
Dim partTim(1000)
Dim partState(1000)
;fire properties
Dim fire(5)
Dim fireX#(5)
Dim fireY#(5)
Dim fireZ#(5)
Dim fireTim(5)
Dim fireState(5)
Dim fireSource(5)
;boat properties
Dim boat(5)
Dim boatPivot(5)
Dim boatX#(5)
Dim boatY#(5)
Dim boatZ#(5)
Dim boatA#(5)
Dim boatState(5)
Dim boatOldState(5)
Dim boatBob#(5)
Dim boatTarget#(5)
;fading in/out
Global fader,fadeAlpha#,fadeTarget#

;----------------------- TEXTS --------------------------
.Texts
;key names
Dim Key$(255)
For count=0 To 255
 Key$(count)="?" 
Next
Key$(2)="1" : Key$(3)="2" : Key$(4)="3" : Key$(5)="4" : Key$(6)="5" : Key$(7)="6" : Key$(8)="7" : Key$(9)="8" : Key$(10)="9"
Key$(16)="Q" : Key$(17)="W" : Key$(18)="E" : Key$(19)="R" : Key$(20)="T" : Key$(21)="Y"
Key$(22)="U" : Key$(23)="I" : Key$(24)="O" : Key$(25)="P" : Key$(26)="[" : Key$(27)="]"
Key$(29)="Left Ctrl" : Key$(30)="A" : Key$(31)="S" : Key$(32)="D" : Key$(33)="F" : Key$(34)="G"
Key$(35)="H" : Key$(36)="J" : Key$(37)="K" : Key$(38)="L" : Key$(39)=";" : Key$(40)="'"
Key$(42)="Left Shift" : Key$(43)="/" : Key$(44)="Z" : Key$(45)="X" : Key$(46)="C" : Key$(47)="V"
Key$(48)="B" : Key$(49)="N" : Key$(50)="M" : Key$(51)="," : Key$(52)="." : Key$(53)="/"
Key$(54)="Right Shift" : Key$(56)="Left Alt" : Key$(157)="Right Ctrl" : Key$(184)="Right Alt"
Key$(57)="Space"
;cameras
Dim textCam$(20)
textCam$(0)="Manual (I,J,K,L,Y,H)"
textCam$(1)="First Person"
textCam$(2)="Close Follow"
textCam$(3)="Raised Follow"
textCam$(4)="Far Follow"
textCam$(5)="High Follow"
textCam$(6)="Bird's Eye"
textCam$(7)="Static"
textCam$(10)="Portrait"
textCam$(11)="Worm's Eye"
textCam$(12)="Heaven Journey"
textCam$(13)="Boat Observer"
textCam$(14)="Ending Shot"
textCam$(15)="Intro Shot"
;OPTIONS
;on/off
Dim textOnOff$(1)
textOnOff$(0)="Off"
textOnOff$(1)="On"
;resolution
Dim textRes$(4)
textRes$(0)="320 x 240"
textRes$(1)="640 x 480"
textRes$(2)="800 x 600"
textRes$(3)="1024 x 768"
textRes$(4)="1280 x 1024"
;game display
Dim textDisplay$(4)
textDisplay$(0)="None"
textDisplay$(1)="Meters Only"
textDisplay$(2)="Meters & Date"
textDisplay$(3)="Meters, Date & Logo"
;music setting
Dim textMusic$(10)
textMusic$(0)="Off"
For count=1 To 10
 textMusic$(count)=(count*10)+"%"
Next
;islands
Dim textIsland$(5)
textIsland$(0)="Random"
textIsland$(1)="North Coast"
textIsland$(2)="East Coast"
textIsland$(3)="South Coast"
textIsland$(4)="West Coast"
textIsland$(5)="Centre"
;cast
Dim textCast$(2)
textCast$(0)="Mixed"
textCast$(1)="Humans"
textCast$(2)="Animals"