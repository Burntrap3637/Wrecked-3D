;//////////////////////////////////////////////////////////////////////////////
;----------------------------- WRECKED: CREDITS -------------------------------
;//////////////////////////////////////////////////////////////////////////////

;---------------------------------------------------------------------
;///////////////////////////// INTRO /////////////////////////////////
;---------------------------------------------------------------------
Function Intro()
;intro material
fontX(2)=LoadFont("EraserDust.ttf",25,0,0,0)
fontX(3)=LoadFont("EraserDust.ttf",36,0,0,0)
gLogo(1)=LoadImage("Graphics/Logo01.png")
MaskImage gLogo(1),255,0,255
gMenuA=LoadImage("Graphics/MenuA.bmp")
MaskImage gMenuA,255,0,255
gMenuB=LoadImage("Graphics/MenuB.bmp")
MaskImage gMenuB,255,0,255
tWater=LoadTexture("World/Sprites/Water.JPG") 
;load setting
gamIsland=3
LoadIsland()
;load atmosphere
camX#(1)=-300 : camY#(1)=100 : camZ#(1)=-500
camType=15 : camFoc(1)=1
LoadAtmos()
;load prop
p(1)=LoadAnimMesh("World/Items/Intro.3ds") ;0
ExtractAnimSeq(p(1),0,40,0) ;1
ExtractAnimSeq(p(1),50,110,0) ;2
ScaleEntity p(1),0.2,0.2,0.2
Animate p(1),1,0.4,1,0
pX#(1)=0 : pZ#(1)=-300
pY#(1)=optSeaLevel#+2 : pA#(1)=200
PositionEntity p(1),pX#(1),pY#(1),pZ#(1)
RotateEntity p(1),0,pA#(1),0
;load particle FX
LoadParticles()
;process ambience
LoopSound sCoast
chAmbience=PlaySound(sCoast)
ChannelVolume chAmbience,0.5
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
If gamDay(1)>0 Then slot=1 Else slot=0
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
	
	;PORTAL
    gotim=gotim+1
	If gotim>50
	 If KeyDown(1) Or KeyDown(28) Or JoyDown(1) Or gotim>300 Then go=1
    EndIf

    ;MANAGE ENTITIES
    ;logo
    If gotim=10 Then PlaySound sJingle
    If gotim=200 Then Animate p(1),3,1,2,0 : ProduceSound(sSplash,11025,1)
    If gotim=235 Then PlaySound sSink
    ;particles
    If gotim<240
     WaterSpurt(pX#(1),pY#(1)-3,pZ#(1)+3)
     WaterSpurt(pX#(1)+25,pY#(1)-1,pZ#(1)+15)
     WaterSpurt(pX#(1)+12,pY#(1)-1,pZ#(1)+9)
     WaterSpurt(pX#(1)-12,pY#(1)-1,pZ#(1)-1)
     WaterSpurt(pX#(1)-25,pY#(1)-1,pZ#(1)-6)
     randy=Rnd(0,20)
     If randy=0 Then ProduceSound(sSplash,11025,Rnd(0.1,0.5))
    EndIf
    ParticleCycle()
    ;camera
    Camera()
  	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 ;mask screen
 If gotim<10 
  Color 0,0,0
  Rect 0,0,GraphicsWidth(),GraphicsHeight(),1
 EndIf

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
;initiate music
Loader("Please Wait","Loading Sound",-1)
StopChannel chAmbience
LoopSound sTheme
chTheme=PlaySound(sTheme)
ChannelPitch chTheme,40000
;remove entities
FreeEntity world
FreeEntity cam(1)
FreeEntity camPivot
FreeEntity light
FreeEntity lightPivot
FreeEntity dummy
;remove prop
FreeEntity p(1)
;remove particles
For cyc=1 To no_particles
 FreeEntity part(cyc)
Next
;proceed
screen=1
End Function

;---------------------------------------------------------------------
;/////////////////////////// 7. CREDITS //////////////////////////////
;---------------------------------------------------------------------
Function Credits()
;camera for fading
cam(1)=CreateCamera()
CameraViewport cam(1),0,0,GraphicsWidth(),GraphicsHeight()
CameraClsMode cam(1),0,1
;sprite for fading
fader=CreateSprite()
ScaleSprite fader,30,30
SpriteViewMode fader,1
PositionEntity fader,0,20,-5
PointEntity cam(1),fader
EntityAlpha fader,fadeAlpha# 
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
foc=1 : oldfoc=foc : scroll#=0
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
	If k=ticks Then CaptureWorld
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 And fadeAlpha#<0.3
	 If KeyDown(1) Then go=-1
	 If KeyDown(28) Or JoyDown(1) Then go=1
	EndIf
	
	;SCROLLER
	If gotim>50 And fadeAlpha#<0.3
	 scroll#=scroll#-1
	 If KeyDown(200) Then scroll#=scroll#-10
	 If KeyDown(208) Then scroll#=scroll#+12
	 If scroll#<-1450 Then scroll#=565
	 If scroll#>565 Then scroll#=-1450
	EndIf
	;scroll tile
	tileX#=tileX#+0.25
	tileY#=tileY#+1.00
	;fade in
    If fadeAlpha#>0 Then fadeAlpha#=fadeAlpha#-0.005
 	EntityAlpha fader,fadeAlpha#  
	
 UpdateWorld
 Next
 ;RenderWorld tween#

 ;DISPLAY
 TileImage gTile,tileX#,tileY#
 DrawImage gLogo(1),rX#(400),rY#(140+scroll#)
 ;header
 SetFont fontX(2)
 Outline("This game was made single-handedly",rX#(400),rY#(254+scroll#),0,0,0,150,125,90)
 Outline("in just 3 months. Consider that...",rX#(400),rY#(281+scroll#),0,0,0,150,125,90) 
 ;credit list
 SetFont fontX(2)
 If currRes=<1 Then x1=260 : x2=540
 If currRes=2 Then x1=280 : x2=520
 If currRes=>3 Then x1=305 : x2=495
 DrawMenu(-1,x1,360+scroll#,"Concept") : DrawMenu(-1,x2,360+scroll#,"© MDickie 2005")
 DrawMenu(-1,x1,440+scroll#,"Game Design") : DrawMenu(-1,x2,440+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,520+scroll#,"Programming") : DrawMenu(-1,x2,520+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,600+scroll#,"2D Graphics") : DrawMenu(-1,x2,600+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,680+scroll#,"3D Modelling") : DrawMenu(-1,x2,680+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,760+scroll#,"Animation") : DrawMenu(-1,x2,760+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,840+scroll#,"Texturing") : DrawMenu(-1,x2,840+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,920+scroll#,"Sound FX") : DrawMenu(-1,x2,920+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,1000+scroll#,"Music") : DrawMenu(-1,x2,1000+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,1080+scroll#,"Narrative") : DrawMenu(-1,x2,1080+scroll#,"Mat Dickie")
 DrawMenu(-1,x1,1160+scroll#,"Publishing") : DrawMenu(-1,x2,1160+scroll#,"Mat Dickie")
 ;extras
 SetFont fontX(2)
 Outline("In memory of the victims of the Tsunami",rX#(400),rY#(1249+scroll#),0,0,0,150,125,90)
 Outline("whose lives were 'Wrecked' for real...",rX#(400),rY#(1276+scroll#),0,0,0,150,125,90)
 DrawImage gMDickie,rX#(400),rY#(1390+scroll#)

 ;allow fading
 RenderWorld 1;tween#
 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
;proceed
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
FreeEntity cam(1)
FreeEntity fader
screen=1
End Function

;---------------------------------------------------------------------
;/////////////////////////// 8. OUTRO ////////////////////////////////
;---------------------------------------------------------------------
Function Outro()
;subdue theme
ChannelVolume chTheme,0.5
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
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
	If k=ticks Then CaptureWorld
	
	;PORTAL
    gotim=gotim+1
	If gotim>50
	 If KeyDown(1) Or KeyDown(28) Or JoyDown(1) Or gotim>600 Then go=1
	EndIf
	;scroll tile
	tileX#=tileX#+1.0
	tileY#=tileY#+0.25
	
 UpdateWorld
 Next
 RenderWorld tween#

 ;DISPLAY
 TileImage gTile,tileX#,tileY#
 DrawImage gOutro,rX#(400),rY#(270)
 DrawImage gLogo(1),rX#(400),rY#(85)
 ;text
 SetFont fontX(3)
 Outline("NO ANIMALS WERE HARMED IN THE",rX#(400),rY#(486),50,0,0,200,0,0)
 Outline("MAKING OF THIS GAME!",rX#(400),rY#(518),50,0,0,200,0,0)
 SetFont font(1)
 Outline("(Although some were upset by the poor modelling inflicted on them by the artist)",rX#(400),rY#(548),0,0,0,180,180,180)
 SetFont font(0)
 Outline("* The artist makes no apologies for any suffering caused by his work.",rX#(400),rY#(575),0,0,0,170,170,170)
 ;Outline("** The animals were further upset that they didn't get an apology.",rX#(400),rY#(585),0,0,0,160,160,160)
 
 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
;leave
SaveProgressData()
SaveOptionData()
screen=0
End Function