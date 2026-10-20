;-------------------------------------------------------------------------
;////////////////////////////// WRECKED //////////////////////////////////
;-------------------------------------------------------------------------
;~~~~~~~~~~~~~~~~~~~~~ Copyright © Mat Dickie 2005 ~~~~~~~~~~~~~~~~~~~~~~~
;~~~~~~~~~~ This program may not be re-released under any other ~~~~~~~~~~
;~~~~~~~ identity or sold commercially without express permission. ~~~~~~~
;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

AppTitle "Wrecked"
;restore & set resolution
Include "Values.bb"
Include "Data.bb"
LoadOptionData()
If optRes=0 Then Graphics3D 320,240,16,1
If optRes=1 Then Graphics3D 640,480,16,1
If optRes=2 Then Graphics3D 800,600,16,1
If optRes=3 Then Graphics3D 1024,768,16,1
If optRes=4 Then Graphics3D 1280,1024,16,1
currRes=optRes
;prepare system
SetBuffer BackBuffer()
HWMultiTex 0
AutoMidHandle True
EnableDirectInput True
SeedRnd(MilliSecs())

;INCLUDES
Include "Functions.bb"
Include "Menus.bb"
Include "Credits.bb"
Include "World.bb"
Include "Players.bb"
Include "AI.bb"
Include "Anims.bb"
Include "Items.bb"
Include "Particles.bb"
Include "Promos.bb"
;initiation process
LoadProgressData()
If gamDay(1)>0 Then loader=1 Else loader=0
LoadWorldData(loader)
Intro()
Loader("Please Wait","Loading Graphics",-1)
LoadImages()
Loader("Please Wait","Loading Textures",-1)
LoadTextures()

;SCREEN MANAGEMENT
SeedRnd(MilliSecs())
screen=1
Repeat
 If screen=1 Then Titles()
 If screen=2 Then Options()
 If screen=3 Then RedefineKeys()
 If screen=4 Then RedefineGamepad()
 If screen=5 Then SlotSelection()
 If screen=6 Then Deathmatch()
 If screen=7 Then Credits()
 If screen=8 Then Outro()
 If screen=10 Then Main()
 If KeyDown(56) And KeyDown(45) Then End
Until screen=0
End 

;--------------------------------------------------------------------------
;////////////////////////// 10. MAIN GAME /////////////////////////////////
;--------------------------------------------------------------------------
Function Main()
SeedRnd(MilliSecs())
;confirm resolution
If currRes<>optRes Then ChangeResolution() 
;load location
If game=>1 Then gamIsland=pIsland(1)
Loader("Please Wait","Loading World",1)
LoadIsland()
;load atmosphere
Loader("Please Wait","Loading Atmosphere",1)
optWeather=0
If KeyDown(19) Then optWeather=1 : PlaySound sSplash
LoadAtmos()
;load characters
Loader("Please Wait","Loading Characters",2)
LoadPlayers()
;load resources
Loader("Please Wait","Loading Resources",3)
LoadResources()
;load items
Loader("Please Wait","Loading Items",3)
LoadItems()
iRange1=((gamIsland-1)*75)+1
iRange2=iRange1+74
;release items for deathmatch
If game=0
 For count=1 To 10
  x#=Rnd(wWest#,wEast#)
  z#=Rnd(wSouth#,wNorth#)
  ReleaseItem(x#,200,z#,Rnd(10,20))
 Next
EndIf
;load boat model
LoadBoat()
;load particle effects
Loader("Please Wait","Loading Effects",3)
LoadParticles()
;fading in/out
fader=CreateSprite()
ScaleSprite fader,20,20
SpriteViewMode fader,1
fadeAlpha#=0 : fadeTarget#=0
EntityAlpha fader,fadeAlpha#
;initiate collisions
Loader("Please Wait","Finalizing World",3)
gamCollisions=0
;introduce ambience
If gamIsland=<4 Then sAmbience=sCoast
If gamIsland=5 Then sAmbience=sAtmos
If optWeather=1 Then sAmbience=sRain
LoopSound sAmbience
chAmbience=PlaySound(sAmbience)
ChannelVolume chAmbience,0.4
If optWeather=1 Then ChannelVolume chAmbience,1.0
;restore fire ambience
If fireState(gamIsland)>0
 LoopSound sFireLoop
 chFire=PlaySound(sFireLoop)
EndIf
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
promoTim=0
gamCheckpoint(slot,2)=0 
gamCheckpoint(slot,5)=0 
optSpeed=1 : optPause=0
go=0 : gotim=0 : keytim=10
While go=0

 Cls
 Repeat
  elapsed=MilliSecs()-time 
 Until elapsed
 ticks=elapsed/period
 tween#=Float(elapsed Mod period)/Float(period)
	 
 For k=1 To ticks
	time=time+period
	;If k=ticks Then CaptureWorld
	
	;COUNTERS
	keytim=keytim-1
	If keytim<1 Then keytim=0 
	camTim=camTim-1
	If camTim<1 Then camTim=0  
	
	;ppppppppppppppppppppppp PAUSE LOOP pppppppppppppppppppppppppppppppppppppp
	If game=<1
	 If (KeyDown(25) And keytim=0) Or (KeyDown(1) And optPause=1)
      keytim=15 : PlaySound sWood
      If optPause=1 Then optPause=0 Else optPause=1
     EndIf
    EndIf 
	If optPause=0
	
	;PORTAL
    gotim=gotim+1
	If gotim>30 And keytim=0
	 ;speed up promo
	 If game=<1 And promo>0 
	  If KeyDown(1) Or KeyDown(28) Or JoyDown(1) Then promoTim=promoTim+150 : keytim=5 : PlaySound sMenuSelect
	 EndIf
	 ;forced exits
	 If promo=0 And gamCheckpoint(slot,5)=1
	  If game=0 And promoTim>250 Then go=-1 ;deathmatch
	  If game=1 And promoTim>100 Then fadeTarget#=1.0 ;failure fade
	  If game=1 And promoTim>500 Then go=-1 ;failure
	  If game=2 And promoTim>300 Then fadeTarget#=1.0 ;completion fade
	  If game=2 And promoTim>700 Then go=-1 ;completion
	 EndIf
	 ;leave
	 If KeyDown(1) And pAnim(1)<>39 And promo=0 And game=<1 Then go=-1
    EndIf

    ;FIDDLES
    If keytim=0 And game=<1
     ;adjust game speed
     If game=1 And gamPlanting=0 And KeyDown(19)=0 And KeyDown(17)=0
      If KeyDown(59) Then optSpeed=1 : period=1000/FPS : PlaySound sMenuBrowse : keytim=10
      If KeyDown(60) Then optSpeed=2 : period=1000/40 : PlaySound sMenuBrowse : keytim=10
      If KeyDown(61) Then optSpeed=3 : period=1000/50 : PlaySound sMenuBrowse : keytim=10
      If KeyDown(62) Then optSpeed=4 : period=1000/60 : PlaySound sMenuBrowse : keytim=10 
     EndIf
     ;adjust game display
     If KeyDown(15) Then optDisplay=optDisplay+1 : PlaySound sMenuBrowse : keytim=10
     If optDisplay>3 Then optDisplay=0
     ;boost health
     If KeyDown(56) And KeyDown(14) And gamPlanting=0
      PlaySound sEat : keytim=10
      pHealth(camFoc(1))=pHealth(camFoc(1))+25 : pDT(camFoc(1))=0
      pHunger(camFoc(1))=pHunger(camFoc(1))-25 : pThirst(camFoc(1))=pThirst(camFoc(1))-25
     EndIf
     ;produce boat
     If KeyDown(48) And KeyDown(20) Then boatState(gamIsland)=9 : PlaySound sSink : keytim=10
     ;release resources
     If KeyDown(19) And gamPlanting=0
      If KeyDown(59) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),1) : PlaySound sFireSinge : keytim=10
      If KeyDown(60) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),2) : PlaySound sFireSinge : keytim=10
      If KeyDown(61) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),3) : PlaySound sFireSinge : keytim=10
      If KeyDown(62) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),4) : PlaySound sFireSinge : keytim=10
      If KeyDown(63) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),5) : PlaySound sFireSinge : keytim=10
     EndIf
     ;release weapons
     If KeyDown(17) And gamPlanting=0
      If KeyDown(59) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),10) : PlaySound sFireSinge : keytim=10
      If KeyDown(60) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),11) : PlaySound sFireSinge : keytim=10
      If KeyDown(61) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),12) : PlaySound sFireSinge : keytim=10
      If KeyDown(62) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),13) : PlaySound sFireSinge : keytim=10
      If KeyDown(63) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),14) : PlaySound sFireSinge : keytim=10
      If KeyDown(64) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),15) : PlaySound sFireSinge : keytim=10
      ;If KeyDown(30) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),17) : PlaySound sFireSinge : keytim=10
      If KeyDown(65) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),18) : PlaySound sFireSinge : keytim=10
      If KeyDown(66) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),19) : PlaySound sFireSinge : keytim=10
      If KeyDown(67) Then ReleaseItem(pX#(camFoc(1)),pY#(camFoc(1))+30,pZ#(camFoc(1)),20) : PlaySound sFireSinge : keytim=10
     EndIf
     ;switch controls
     If optMulti=0 Or game=>1
      If KeyDown(56) Or game=0
       If KeyDown(12) Then SwitchControl(-1) : PlaySound sMenuBack : keytim=10
       If KeyDown(13) Then SwitchControl(1) : PlaySound sMenuGo : keytim=10
      EndIf
     EndIf
     ;browse resources left
     If KeyDown(59) And gamPlanting=1
      PlaySound sWood : keytim=7
      Repeat
       gamPlanter=gamPlanter-1
       If gamPlanter<1 Then gamPlanter=no_resources 
      Until resIsland(gamPlanter)=gamIsland
     EndIf
     ;browse resources right
     If KeyDown(60) And gamPlanting=1 
      PlaySound sWood : keytim=7
      Repeat
       gamPlanter=gamPlanter+1
       If gamPlanter>no_resources  Then gamPlanter=1
      Until resIsland(gamPlanter)=gamIsland
     EndIf
     ;remove character
     If KeyDown(211) And KeyDown(46) And game=1
      PlaySound sSink : keytim=10 : its=0
      Repeat
       v=Rnd(1,no_plays)
       its=its+1
       If its>50 Then v=0
      Until v=0 Or (pIsland(v)=gamIsland And pState(v)>0 And pType(v)=>4)
      If v>0 Then pState(v)=0 ;: pIsland(v)=0
     EndIf
     ;remove resource
     If KeyDown(211) And KeyDown(19)
      PlaySound sSink : keytim=10 : its=0
      Repeat
       v=Rnd(1,no_resources)
       its=its+1
       If its>50 Then v=0
      Until v=0 Or (resIsland(v)=gamIsland And resState(v)>0)
      If v>0 Then resState(v)=0 ;: resIsland(v)=0
     EndIf
    EndIf
 
    ;PLAYER CYCLE
    PlayerCycle()

    ;RESOURCE CYCLE
    ResourceCycle()
     
    ;ITEM CYCLE
    ItemCycle()
    
    ;BOAT CYCLE
    BoatCycle()

    ;PARTICLE CYCLE
    ParticleCycle()
    ;manage fires
    FireCycle()

    ;ATMOSPHERE
    ManageAtmosphere()

    ;MANAGE MUSIC
    ;adjust volume
    musicTVol#=Float(optMusic)/10
    If promo>0 Or game=2 Then musicTVol#=1.0
    If pAnim(1)=39 Or pDT(1)>999 Then musicTVol#=1.0
    If game=0 And gamCheckpoint(slot,5)=1 Then musicTVol#=1.0
    If promo>0 And game=2 Then musicTVol#=0.4 
    If musicVol#<musicTVol# Then musicVol#=musicVol#+0.005
    If musicVol#>musicTVol# Then musicVol#=musicVol#-0.005
    ChannelVolume chTheme,musicVol#
    ;tie pitch to soul
    If musicVol#>0 And game=1
     If gamSoul(slot)=1 Then ChannelPitch chTheme,30000
     If gamSoul(slot)=2 Then ChannelPitch chTheme,37500
     If gamSoul(slot)=3 Then ChannelPitch chTheme,40000
     If game=2 And gamSoul(slot)=>2 And MissionsLeft()>0 Then ChannelPitch chTheme,30000
     If pAnim(1)=39 Or pDT(1)>999 Then ChannelPitch chTheme,25000
    EndIf 
   
    ;GAME PROGRESS
    ;monitor deathmatch
    If game=0
     remaining=CountAnimals(0)
     If remaining=<1 And gamCheckpoint(slot,5)=0 
      camType=10 : camFoc(1)=gamSurvivor : camFoc(2)=gamSurvivor
      promoTim=0 : gamCheckpoint(slot,5)=1
     EndIf
    EndIf
    ;promo issues
    FindPromos(1)
    ;leave island
    LeaveIsland(1)

    ;pppppppppppppppppppppp END OF PAUSE LOOP pppppppppppppppppppppppppppppppp
    EndIf

    ;CAMERA
    Camera()
	
 If optPause=0 Then UpdateWorld
 Next
 If no_cameras=2 Then TileImage gTile,tileX#,tileY#
 RenderWorld 1
 If gotim>1 And gamCollisions=0 Then EnableCollisions()

 ;DISPLAY
If fadeAlpha#=0
 If no_cameras=2 Then Color 0,0,0 : Line rX#(400),rY#(120),rX#(400),rY#(480)
 If optDisplay=>3 And promo=0 And game=<1 Then DrawImage gLogo(2),rX#(400),rY#(60)
 ;calendar
 If optDisplay=>2 And promo=0 And currRes>0 And game=1
  DrawImage gCalendar,rX#(720),rY#(50)
  SetFont fontX(4)
  Outline(gamDay(slot),rX#(720),rY#(58),0,0,0,130,105,70)
 EndIf
 ;game speed
 If optSpeed=1 And pAnim(camFoc(1))=37 And game=1 And camTim=0
  SetFont fontX(2)
  Outline("Use keys F1-F4 to",rX#(130),rY#(50),0,0,0,150,125,90)
  Outline("speed up time...",rX#(130),rY#(70),0,0,0,150,125,90)
 EndIf
 If optSpeed>1 And camTim=0
  x=100 : y=60
  For count=1 To optSpeed-1
   DrawImage gSpeed,rX#(x),rY#(y)
   x=x+30
  Next
 EndIf
 ;camera
 If camTim>10 And game=<1
  DrawImage gCamera,rX#(55),rY#(40)
  SetFont fontX(2)
  OutlineStraight(textCam$(camType),rX#(95),rY#(46),0,0,0,150,125,90)
 EndIf
 ;METERS
 If optDisplay=>1 And promo=0 And currRes>0 And game=<1
  For cammy=1 To no_cameras
   cyc=camFoc(cammy)
   If no_cameras=1 Then x=150 : y=550
   If no_cameras=2 And cammy=1 Then x=200 : y=530
   If no_cameras=2 And cammy=2 Then x=600 : y=530
   If no_cameras=1 Then range=3 Else range=1
   For count=1 To range
    ;initial meters
    DrawImage gMeter(0),rX#(x),rY#(y)+10
    If count=1 Then meter#=pHealth(cyc)*1.5
    If count=2 Then meter#=pHunger(cyc)*1.5
    If count=3 Then meter#=pThirst(cyc)*1.5
    DrawImageRect gMeter(count),rX#(x),rY#(y)+10,0,0,meter#,24
    ;alerts!
    If (count=1 And pHealth(cyc)=<20) Or (count=2 And pHunger(cyc)=>80) Or (count=3 And pThirst(cyc)=>80)
     DrawImageRect gMeter(4),rX#(x),rY#(y)+10,0,0,meter#,24 
    EndIf
    ;establish line
    Color 0,0,0
    plotX#=(rX#(x)-75)+meter#
    Line plotX#+1,rY#(y)-2,plotX#+1,rY#(y)+22
    ;indicate fire
    If count=1 And FireProximity(cyc,30) ;And (pAnim(cyc)=33 Or pAnim(cyc)=37)
     Color 255,Rnd(50,150),0
     Rect rX#(x)-73,rY#(y)-1,147,22,0
    EndIf
    ;cover with header
    DrawImage gHeader(count),rX#(x),rY#(y)
    x=x+250
   Next
  Next
 EndIf
 ;DIAGNOSTICS
 If KeyDown(56) And KeyDown(32) 
  SetFont font(1)
  y=300
  For count=1 To 5
   Outline(textIsland$(count)+": "+fireState(count),rX#(100),rY#(y),0,0,0,200,200,200)
   y=y+15
  Next
  Outline("pA: "+pA#(1),rX#(100),rY#(80),0,0,0,200,200,200)
  Outline("pX: "+pX#(1),rX#(100),rY#(100),0,0,0,240,240,240)
  Outline("pY: "+pY#(1),rX#(100),rY#(115),0,0,0,240,240,240)
  Outline("pZ: "+pZ#(1),rX#(100),rY#(130),0,0,0,240,240,240)
  Outline("pGround: "+pGround#(1),rX#(100),rY#(150),0,0,0,240,240,240)
  Outline("Gravity: "+pGravity#(1),rX#(100),rY#(175),0,0,0,240,240,240)  
  Outline("Island: "+gamIsland+" ("+CountPopulation(gamIsland)+")",rX#(700),rY#(100),50,0,0,240,240,200)
  Outline("gamPlanter: "+gamPlanter,rX#(700),rY#(120),0,50,0,100,250,100) 
  Outline("("+resName$(resType(gamPlanter))+")",rX#(700),rY#(135),0,50,0,100,250,100)  
  Outline("Dog Agenda: "+pAgenda(42),rX#(700),rY#(150),0,50,0,100,250,100)
  Outline("Dog Target: "+pTarget(42),rX#(700),rY#(165),0,50,0,100,250,100)
  Outline("gamTime: "+gamTime(slot),rX#(700),rY#(200),50,0,0,240,240,200)
  Outline("gamDay: "+gamDay(slot),rX#(700),rY#(215),50,0,0,240,240,200)
  Outline("Soul: "+gamSoul(slot)+" : "+pEyes(1),rX#(650),rY#(315),50,0,0,240,240,200)
  Outline("Missions: "+CountMissions()+" / "+MissionsLeft(),rX#(650),rY#(330),50,0,0,240,240,200)
  Outline("Sins: "+CountCrimes(),rX#(650),rY#(345),50,0,0,240,240,200)
  Outline("Latest Crime: "+gamSins(slot,gamCrime(slot)),rX#(650),rY#(360),50,0,0,240,240,200)
 EndIf
 ;reset expressions
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
  If game=>1 And pType(cyc)=1 Then pEyes(cyc)=gamSoul(slot)
  If game=>1 And pType(cyc)=2 Then pEyes(cyc)=1
  If game=>1 And pType(cyc)=3 Then pEyes(cyc)=3
 Next
 ;display subtitles
 If promo>0 Then DisplayPromo()
 ;paused
 If optPause=1 
  DrawImage gPaused,rX#(400),rY#(290)
  SetFont fontX(2)
  Outline("(Press 'P' again to resume play)",rX#(400),rY#(345),0,0,0,150,125,90)
 EndIf
 ;mask shaky start!
 If gotim=<20 Then Loader("Please Wait","Finalizing World",3)
EndIf

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()
 ;save preview shot
 If game=1 And go=-1 And gamCheckpoint(slot,5)=0
  gPreview(slot)=CreateImage(GraphicsWidth(),GraphicsHeight())
  GrabImage gPreview(slot),GraphicsWidth()/2,GraphicsHeight()/2
 EndIf

Wend
;restore sound
StopChannel chAmbience
If ChannelPlaying(chFire) Then StopChannel chFire
musicVol#=1.0
ChannelVolume chTheme,musicVol#
;process preview photo
If game=1 And go=-1 And gamCheckpoint(slot,5)=0
 Loader("Please Wait","Saving Progress",0)
 ResizeImage gPreview(slot),200,150
 SaveImage gPreview(slot),"Data/Save0"+slot+"/Preview.bmp"
 MaskImage gPreview(slot),255,0,255
EndIf
;remove atmospherics
FreeEntity world
For cyc=1 To no_cameras
 FreeEntity cam(cyc)
Next
FreeEntity camPivot
FreeEntity light
FreeEntity lightPivot
FreeEntity dummy
If optWeather=1
 For count=1 To 20
  FreeEntity rain(count)
 Next
EndIf
;remove players
For cyc=1 To no_plays
 If pIsland(cyc)=gamIsland
  FreeEntity p(cyc)
  FreeEntity pPivot(cyc)
  FreeEntity pShadow(cyc)
 EndIf
Next
;remove resources
For cyc=1 To no_resources
 If resIsland(cyc)=gamIsland Then FreeEntity res(cyc)
Next
;remove items
For cyc=1 To no_items
 If iIsland(cyc)=gamIsland Then FreeEntity i(cyc)
Next
;remove boat
If gamIsland<>5 
 FreeEntity boat(gamIsland)
 FreeEntity boatPivot(gamIsland) 
EndIf
;remove particles
For cyc=1 To no_particles
 FreeEntity part(cyc)
Next
FreeEntity fader
;transfer player & items
ClearCollisions
gamJourney=0
If gamDestination>0
 RelocatePlayer(1,gamIsland,gamDestination)
 RelocateEnemies() 
 If pAnim(1)=5 ;prepare for ending
  game=2 : gamJourney=0 : boatState(1)=9
  boatX#(1)=500 : boatZ#(1)=500 : boatY#(1)=10
  camX#(1)=600 : camY#(1)=60 : camZ#(1)=400
 EndIf
EndIf
;save data
If game=>1 And slot>0
 If gamCheckpoint(slot,5)=1 Then gamDay(slot)=0
 SaveProgressData()
 SaveWorldData(slot)
 SavePlayerData(slot)
 SaveResourceData(slot)
 SaveItemData(slot)
EndIf
;proceed
screen=1
If game=0 Then screen=6 ;back to deathmatch set-up
If go=1 And game=>1 Then screen=10 ;new island
If go=-1 And game=1 Then screen=5 ;back to slot selection
If game=>1 And gamCheckpoint(slot,5)=1 Then screen=7 ;change to credits!
End Function

;ENABLE COLLISIONS
Function EnableCollisions()
 Collisions 1,10,2,3 ;player >>> scenery
 Collisions 2,10,2,3 ;shadows >>> scenery
 Collisions 3,10,2,1 ;items >>> scenery
 Collisions 4,10,2,1 ;resources >>> scenery
 If game=<1 Then Collisions 20,10,2,3 ;camera >>> scenery
 If game=<1 Then Collisions 20,11,2,3 ;camera >>> superfluous scenery
 Collisions 1,4,2,3 ;player >>> resources
 Collisions 2,4,2,3 ;shadows >>> resources
 Collisions 3,4,2,2 ;items >>> resources
 gamCollisions=1
End Function

;PROGRESS TO NEW ISLAND
Function LeaveIsland(cyc)
 gamDestination=0
 If game=1
  ;find exits
  If pAnim(cyc)=1
   If pZ#(cyc)>wNorth#+300 Then gamDestination=wNorthLead ;north route
   If pZ#(cyc)<wSouth#-300 Then gamDestination=wSouthLead ;south route
   If pX#(cyc)>wEast#+300 Then gamDestination=wEastLead ;east route
   If pX#(cyc)<wWest#-300 Then gamDestination=wWestLead ;west route
  EndIf
  ;boat exits
  If pAnim(cyc)=5 And boatState(gamIsland)=>9
   If gamIsland=1 And pZ#(cyc)>wNorth#+100 Then gamDestination=1 ;north coast
   If gamIsland=2 And pX#(cyc)>wEast#+100 Then gamDestination=1 ;east coast
   If gamIsland=3 And pZ#(cyc)<wSouth#-100 Then gamDestination=1 ;south coast
   If gamIsland=4 And pX#(cyc)<wWest#-100 Then gamDestination=1 ;west coast
  EndIf
  ;implement change
  If gamDestination>0 Then go=1
 EndIf
End Function

;RELOCATE PLAYER
Function RelocatePlayer(cyc,oldIsland,newIsland)
 pX#(cyc)=0 : pY#(cyc)=100 : pZ#(cyc)=0
 ;north coast arrivals
 If newIsland=1 
  If oldIsland=5 ;south route
   pX#(cyc)=-48 : pY#(cyc)=100 : pZ#(cyc)=-897 : pA#(cyc)=0 
   If cyc=1 Then gamJourney=1 : travelX#=401 : travelY#=257
  EndIf
  If oldIsland=2 ;east route
   pX#(cyc)=803 : pY#(cyc)=84 : pZ#(cyc)=-151 : pA#(cyc)=90 
   If cyc=1 Then gamJourney=12 : travelX#=460 : travelY#=241
  EndIf
  If oldIsland=4 ;west route
   pX#(cyc)=-920 : pY#(cyc)=78 : pZ#(cyc)=-144 : pA#(cyc)=270 
   If cyc=1 Then gamJourney=10 : travelX#=343 : travelY#=240
  EndIf
 EndIf
 ;east coast arrivals
 If newIsland=2 
  If oldIsland=1 ;north route
   pX#(cyc)=-200 : pY#(cyc)=24 : pZ#(cyc)=921 : pA#(cyc)=180
   If cyc=1 Then gamJourney=11 : travelX#=460 : travelY#=241
  EndIf
  If oldIsland=5 ;west route
   pX#(cyc)=-962 : pY#(cyc)=55 : pZ#(cyc)=4 : pA#(cyc)=270 
   If cyc=1 Then gamJourney=4 : travelX#=447 : travelY#=295
  EndIf
  If oldIsland=3 ;south route
   pX#(cyc)=-201 : pY#(cyc)=24 : pZ#(cyc)=-866 : pA#(cyc)=0
   If cyc=1 Then gamJourney=7 : travelX#=461 : travelY#=352 
  EndIf
 EndIf
 ;south coast arrivals
 If newIsland=3 
  If oldIsland=5 ;north route
   pX#(cyc)=-22 : pY#(cyc)=55 : pZ#(cyc)=975 : pA#(cyc)=180 
   If cyc=1 Then gamJourney=2 : travelX#=402 : travelY#=333
  EndIf
  If oldIsland=2 ;east route
   pX#(cyc)=914 : pY#(cyc)=28 : pZ#(cyc)=195 : pA#(cyc)=90 
   If cyc=1 Then gamJourney=8 : travelX#=461 : travelY#=352
  EndIf
  If oldIsland=4 ;west route
   pX#(cyc)=-942 : pY#(cyc)=24 : pZ#(cyc)=189 : pA#(cyc)=270
   If cyc=1 Then gamJourney=6 : travelX#=343 : travelY#=351
  EndIf
 EndIf
 ;west coast arrivals
 If newIsland=4 
  If oldIsland=1 ;north route
   pX#(cyc)=177 : pY#(cyc)=35 : pZ#(cyc)=948 : pA#(cyc)=180 
   If cyc=1 Then gamJourney=9 : travelX#=343 : travelY#=240
  EndIf
  If oldIsland=5 ;east route
   pX#(cyc)=905 : pY#(cyc)=57 : pZ#(cyc)=27 : pA#(cyc)=90 
   If cyc=1 Then gamJourney=3 : travelX#=357 : travelY#=295
  EndIf
  If oldIsland=3 ;south route
   pX#(cyc)=164 : pY#(cyc)=27 : pZ#(cyc)=-929 : pA#(cyc)=0 
   If cyc=1 Then gamJourney=5 : travelX#=343 : travelY#=351
  EndIf
 EndIf
 ;centre arrivals
 If newIsland=5 
  If oldIsland=1 ;north route
   pX#(cyc)=-16 : pY#(cyc)=45 : pZ#(cyc)=767 : pA#(cyc)=180 
   If cyc=1 Then gamJourney=2 : travelX#=401 : travelY#=257
  EndIf
  If oldIsland=2 ;east route
   pX#(cyc)=882 : pY#(cyc)=25 : pZ#(cyc)=8 : pA#(cyc)=90 
   If cyc=1 Then gamJourney=3 : travelX#=447 : travelY#=295
  EndIf
  If oldIsland=3 ;south route
   pX#(cyc)=2 : pY#(cyc)=46 : pZ#(cyc)=-787 : pA#(cyc)=0 
   If cyc=1 Then gamJourney=1 : travelX#=402 : travelY#=333
  EndIf
  If oldIsland=4 ;west route
   pX#(cyc)=-882 : pY#(cyc)=23 : pZ#(cyc)=-8 : pA#(cyc)=270 
   If cyc=1 Then gamJourney=4 : travelX#=357 : travelY#=295
  EndIf
 EndIf
 ;update other values
 pIsland(cyc)=newIsland
 If pCarryA(cyc)>0 Then iIsland(pCarryA(cyc))=pIsland(cyc)
 If pCarryB(cyc)>0 Then iIsland(pCarryB(cyc))=pIsland(cyc)
 pOldX#(cyc)=pX#(cyc) : pOldY#(cyc)=pY#(cyc) : pOldZ#(cyc)=pZ#(cyc)
 pNowhere(cyc)=99
 ;prepare camera
 If cyc=1 Then camX#(1)=0 : camY#(1)=100 : camZ#(1)=0
End Function

;RELOCATE ENEMIES
Function RelocateEnemies()
 For cyc=1 To no_plays
  ;switch coast
  If pType(cyc)<10 Then randy=Rnd(0,10) Else randy=Rnd(0,20)
  If cyc=33 And gamCheckpoint(slot,1)=0 Then randy=99
  If randy=0 And pType(cyc)<>1 And pType(cyc)<>3 And pType(cyc)<>20 And cyc<>42 
   ;find sensible destination
   its=0
   Repeat
    satisfied=1
    newIsland=Rnd(1,5)
    If newIsland=pIsland(cyc) Or newIsland=gamDestination Then satisfied=0
    If newIsland=5 And pIsland(cyc)=4 And resState(121)=1 Then satisfied=0
    If newIsland=>4 And CountPopulation(newIsland)=>11 Then satisfied=0
    If CountPopulation(newIsland)=>14 Then satisfied=0
    its=its+1
    If its>50 Then satisfied=1 : newIsland=0
   Until satisfied=1
   ;execute transfer
   If newIsland>0 Then RelocatePlayer(cyc,pIsland(cyc),newIsland)
  EndIf
  ;regenerate fish
  randy=Rnd(0,10)
  If randy=0 And pType(cyc)=20 And pState(cyc)=<0 
   pState(cyc)=1 : pHealth(cyc)=20 : pHP(cyc)=5
  EndIf
 Next
End Function

;COUNT POPULATION
Function CountPopulation(island)
 value=0
 For cyc=1 To no_plays
  If pIsland(cyc)=island And pState(cyc)>0 Then value=value+1
 Next
 Return value
End Function