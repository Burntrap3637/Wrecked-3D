;//////////////////////////////////////////////////////////////////////////////
;-------------------------- WRECKED: MENU SCREENS -----------------------------
;//////////////////////////////////////////////////////////////////////////////

;---------------------------------------------------------------------
;/////////////////////////// 1. TITLES ///////////////////////////////
;---------------------------------------------------------------------
Function Titles()
;restore theme
musicVol#=1.0
ChannelVolume chTheme,musicVol#
ChannelPitch chTheme,40000
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
game=0 : foc=1 : oldfoc=foc
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
	If gotim>20
	 If KeyDown(1) And gotim>35 Then go=-1
	 If KeyDown(28) Or JoyDown(1) 
	  If foc=<4 Then go=1
	  If foc=5 Then go=-1 
	 EndIf
	EndIf
	
	;CONFIGURATION
	If keytim=0
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=6 : PlaySound sMenuSelect
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=6 : PlaySound sMenuSelect
	EndIf
	;limits
	If foc>5 Then foc=1
	If foc<1 Then foc=5 
	
	;scroll tile
	tileX#=tileX#+1.0
	tileY#=tileY#+0.25
	
 UpdateWorld
 Next
 RenderWorld tween#

 ;DISPLAY
 TileImage gTile,tileX#,tileY#
 DrawImage gLogo(1),rX#(400),rY#(140)
 DrawImage gMDickie,rX#(120),rY#(535)
 DrawImage gMDickie,rX#(680),rY#(535) 
 ;options
 y=270
 SetFont fontX(2)
 DrawMenu(1,400,y,"ADVENTURE")
 DrawMenu(2,400,y+60,"DEATHMATCH")
 DrawMenu(3,400,y+120,"OPTIONS")
 DrawMenu(4,400,y+180,"CREDITS")
 DrawMenu(5,400,540,"<<< QUIT <<<")
 ;version
 SetFont font(1)
 Color 105,135,115
 Text rX#(530),rY#(190),"Version 1.1",1,1

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
;proceed
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
If go=1 
 If foc=1 Then screen=5
 If foc=2 Then screen=6
 If foc=3 Then screen=2 
 If foc=4 Then screen=7  
EndIf
If go=-1 Then screen=8
End Function

;---------------------------------------------------------------------
;/////////////////////////// 2. OPTIONS //////////////////////////////
;---------------------------------------------------------------------
Function Options()
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
foc=1 : oldfoc=foc
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
	If gotim>20 And keytim=0
	 If KeyDown(1) Then go=-1
	 If KeyDown(28) Or JoyDown(1)
	  If foc=6 Or foc=7 Then go=1
	  If foc=8 And keytim=0 Then go=-1 
	  If foc=<5 Then foc=8 : keytim=10 : PlaySound sMenuSelect
	 EndIf
	EndIf
	
	;CONFIGURATION
	If keytim=0
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=6 : PlaySound sMenuSelect
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=6 : PlaySound sMenuSelect
	 If foc>8 Then foc=1
	 If foc<1 Then foc=8 
	 ;browse left
	 If KeyDown(203) Or JoyXDir()=-1 
	  PlaySound sMenuBrowse : keytim=8
	  If foc=1 Then optRes=optRes-1
	  If foc=2 Then optMusic=optMusic-1 : keytim=5
	  If foc=3 Then optAtmos=optAtmos-1
	  If foc=4 Then optFX=optFX-1
	  If foc=5 Then optBlood=optBlood-1
	 EndIf
	 ;browse right
	 If KeyDown(205) Or JoyXDir()=1 
	  PlaySound sMenuBrowse : keytim=8
	  If foc=1 Then optRes=optRes+1
	  If foc=2 Then optMusic=optMusic+1 : keytim=5
	  If foc=3 Then optAtmos=optAtmos+1
	  If foc=4 Then optFX=optFX+1
	  If foc=5 Then optBlood=optBlood+1
	 EndIf
	EndIf
	;limits
	If optRes<1 Then optRes=1
	If optRes>4 Then optRes=4
	If optMusic<0 Then optMusic=0
	If optMusic>10 Then optMusic=10
	If optAtmos<0 Then optAtmos=1
	If optAtmos>1 Then optAtmos=0
	If optFX<0 Then optFX=1
	If optFX>1 Then optFX=0
	If optBlood<0 Then optBlood=1
	If optBlood>1 Then optBlood=0     
	
	;scroll tile
	tileX#=tileX#+1.0
	tileY#=tileY#+0.25 
	
 UpdateWorld
 Next
 RenderWorld tween#

 ;DISPLAY
 TileImage gTile,tileX#,tileY#
 DrawImage gLogo(2),rX#(400),rY#(55)
 DrawImage gMDickie,rX#(120),rY#(535)
 DrawImage gMDickie,rX#(680),rY#(535)
 ;options
 y=135
 If currRes=<1 Then x1=260 : x2=540
 If currRes=2 Then x1=280 : x2=520
 If currRes=>3 Then x1=305 : x2=495
 SetFont fontX(2)
 DrawMenu(1,x1,y,"Resolution") : DrawMenu(1,x2,y,textRes$(optRes))
 DrawMenu(2,x1,y+55,"In-Game Music") : DrawMenu(2,x2,y+55,textMusic$(optMusic))
 DrawMenu(3,x1,y+110,"Atmosphere") : DrawMenu(3,x2,y+110,textOnOff$(optAtmos))
 DrawMenu(4,x1,y+165,"Particle FX") : DrawMenu(4,x2,y+165,textOnOff$(optFX))
 DrawMenu(5,x1,y+220,"Blood & Gore") : DrawMenu(5,x2,y+220,textOnOff$(optBlood))
 DrawMenu(6,400,y+280,"REDEFINE KEYS")
 DrawMenu(7,400,y+335,"REDEFINE GAMEPAD") 
 DrawMenu(8,400,540,"<<< SAVE & EXIT <<<")

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
;proceed
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
If go=1 And foc=6 Then screen=3 ;keys
If go=1 And foc=7 Then screen=4 ;pad
If go=-1 Then SaveOptionData() : screen=1
End Function

;---------------------------------------------------------------------
;////////////////////// 3. REDEFINE KEYS /////////////////////////////
;---------------------------------------------------------------------
Function RedefineKeys()
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
foc=1 : oldfoc=foc
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
	If gotim>20 And keytim=0 
	 If KeyDown(1) Then go=-1
	 If KeyDown(28) Or JoyDown(1)
	  If foc=8 Then go=-1 
	 EndIf
	EndIf
	
	;CONFIGURE
	If keytim=0
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=6 : PlaySound sMenuSelect
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=6 : PlaySound sMenuSelect
	 If foc>8 Then foc=1
	 If foc<1 Then foc=8
	 ;enter new command
	 If KeyDown(28) Or JoyDown(1)
	  If gotim>20 And foc=<6
	   keytim=10
	   TileImage gTile,tileX#,tileY#
 	   DrawImage gLogo(2),rX#(400),rY#(55)
	   SetFont fontX(4)
       Outline("Press new key...",rX#(400),rY#(250),0,0,0,150,125,90)
       Flip
	   If foc=1 Then keyAttackA=AssignKey()
	   If foc=2 Then keyAttackB=AssignKey()
	   If foc=3 Then keyPickA=AssignKey()
	   If foc=4 Then keyPickB=AssignKey()
	   If foc=5 Then keyJump=AssignKey()
	   If foc=6 Then keySleep=AssignKey()	   
	  EndIf
	 EndIf
	 ;restore defaults
	 If KeyDown(28) Or JoyDown(1)
	  If foc=7
	   PlaySound sTrash : keytim=10
	   keyAttackA=30 : keyAttackB=31 : keyJump=57
       keyPickA=44 : keyPickB=45 : keySleep=29
	  EndIf
	 EndIf
	EndIf
	
	;scroll tile
	tileX#=tileX#+1.0
	tileY#=tileY#+0.25
	
 UpdateWorld
 Next
 RenderWorld tween#

 ;DISPLAY
 TileImage gTile,tileX#,tileY#
 DrawImage gLogo(2),rX#(400),rY#(55)
 DrawImage gMDickie,rX#(120),rY#(535)
 DrawImage gMDickie,rX#(680),rY#(535)
 ;options
 y=135
 If currRes=<1 Then x1=260 : x2=540
 If currRes=2 Then x1=280 : x2=520
 If currRes=>3 Then x1=305 : x2=495
 SetFont fontX(2)
 DrawMenu(1,x1,y,"Left Attack") : DrawMenu(1,x2,y,Key$(keyAttackA))
 DrawMenu(2,x1,y+55,"Right Attack") : DrawMenu(2,x2,y+55,Key$(keyAttackB))
 DrawMenu(3,x1,y+110,"Left Pick-Up") : DrawMenu(3,x2,y+110,Key$(keyPickA))
 DrawMenu(4,x1,y+165,"Right Pick-Up") : DrawMenu(4,x2,y+165,Key$(keyPickB))
 DrawMenu(5,x1,y+220,"Jump") : DrawMenu(5,x2,y+220,Key$(keyJump))
 DrawMenu(6,x1,y+275,"Sleep") : DrawMenu(6,x2,y+275,Key$(keySleep))
 DrawMenu(7,400,y+340,"RESTORE DEFAULTS") 
 DrawMenu(8,400,540,"<<< SAVE & EXIT <<<")
 ;advice
 SetFont fontX(1)
 If foc=1 Then Outline("(Also used for throwing and building)",rX#(400),rY#(y+22),0,0,0,200,50,50)
 If foc=2 Then Outline("(Also used for throwing and building)",rX#(400),rY#(y+77),0,0,0,200,50,50)
 If foc=3 Then Outline("(Also used for dropping and consuming)",rX#(400),rY#(y+132),0,0,0,200,50,50)
 If foc=4 Then Outline("(Also used for dropping and consuming)",rX#(400),rY#(y+187),0,0,0,200,50,50)
 If foc=6 Then Outline("(Also available by pressing both Pick-Up commands)",rX#(400),rY#(y+297),0,0,0,200,50,50)
 
 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
If go=1 Then SaveOptionData()
screen=2
End Function

;------------------------------------------------------------------------
;////////////////////// 4. REDEFINE GAMEPAD /////////////////////////////
;------------------------------------------------------------------------
Function RedefineGamepad()
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
foc=1 : oldfoc=foc
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
	If gotim>20 And keytim=0 
	 If KeyDown(1) Then go=-1
	 If KeyDown(28) Or JoyDown(1)
	  If foc=8 Then go=-1 
	 EndIf
	EndIf
	
	;CONFIGURE
	If keytim=0
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=6 : PlaySound sMenuSelect
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=6 : PlaySound sMenuSelect
	 If foc>8 Then foc=1
	 If foc<1 Then foc=8
	 ;enter new command
	 If KeyDown(28)
	  If gotim>20 And foc=<6
	   keytim=10
	   TileImage gTile,tileX#,tileY#
 	   DrawImage gLogo(2),rX#(400),rY#(55)
	   SetFont fontX(4)
       Outline("Press new button...",rX#(400),rY#(250),0,0,0,150,125,90)
       SetFont fontX(2)
       Outline("(If your gamepad has run out of buttons, you can",rX#(400),rY#(350),0,0,0,200,50,50)
       Outline("forfeit this command by pressing BACKSPACE)",rX#(400),rY#(370),0,0,0,200,50,50)
       Flip
	   If foc=1 Then buttAttackA=AssignButton()
	   If foc=2 Then buttAttackB=AssignButton()
	   If foc=3 Then buttPickA=AssignButton()
	   If foc=4 Then buttPickB=AssignButton()
	   If foc=5 Then buttJump=AssignButton()
	   If foc=6 Then buttSleep=AssignButton()	   
	  EndIf
	 EndIf
	 ;restore defaults
	 If KeyDown(28) Or JoyDown(1)
	  If foc=7
	   PlaySound sTrash : keytim=10
	   buttAttackA=4 : buttAttackB=5 : buttJump=6
	   buttPickA=1 : buttPickB=2 : buttSleep=3
	  EndIf
	 EndIf
	EndIf
	
	;scroll tile
	tileX#=tileX#+1.0
	tileY#=tileY#+0.25
	
 UpdateWorld
 Next
 RenderWorld tween#

 ;DISPLAY
 TileImage gTile,tileX#,tileY#
 DrawImage gLogo(2),rX#(400),rY#(55)
 DrawImage gMDickie,rX#(120),rY#(535)
 DrawImage gMDickie,rX#(680),rY#(535)
 ;options
 y=135
 If currRes=<1 Then x1=260 : x2=540
 If currRes=2 Then x1=280 : x2=520
 If currRes=>3 Then x1=305 : x2=495
 SetFont fontX(2)
 DrawMenu(1,x1,y,"Left Attack") : DrawMenu(1,x2,y,"Button "+buttAttackA)
 DrawMenu(2,x1,y+55,"Right Attack") : DrawMenu(2,x2,y+55,"Button "+buttAttackB)
 DrawMenu(3,x1,y+110,"Left Pick-Up") : DrawMenu(3,x2,y+110,"Button "+buttPickA)
 DrawMenu(4,x1,y+165,"Right Pick-Up") : DrawMenu(4,x2,y+165,"Button "+buttPickB)
 DrawMenu(5,x1,y+220,"Jump") : DrawMenu(5,x2,y+220,"Button "+buttJump)
 DrawMenu(6,x1,y+275,"Sleep") : DrawMenu(6,x2,y+275,"Button "+buttSleep)
 DrawMenu(7,400,y+340,"RESTORE DEFAULTS") 
 DrawMenu(8,400,540,"<<< SAVE & EXIT <<<")
 ;advice
 SetFont fontX(1)
 If foc=1 Then Outline("(Also used for throwing and building)",rX#(400),rY#(y+22),0,0,0,200,50,50)
 If foc=2 Then Outline("(Also used for throwing and building)",rX#(400),rY#(y+77),0,0,0,200,50,50)
 If foc=3 Then Outline("(Also used for dropping and consuming)",rX#(400),rY#(y+132),0,0,0,200,50,50)
 If foc=4 Then Outline("(Also used for dropping and consuming)",rX#(400),rY#(y+187),0,0,0,200,50,50)
 If foc=6 Then Outline("(Also available by pressing both Pick-Up commands)",rX#(400),rY#(y+297),0,0,0,200,50,50)
 
 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
If go=1 Then SaveOptionData()
screen=2
End Function

;---------------------------------------------------------------------
;//////////////////////// 5. SLOT SELECTION //////////////////////////
;---------------------------------------------------------------------
Function SlotSelection()
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
If slot>0 Then foc=slot Else foc=1
game=1 : oldfoc=foc
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
	If gotim>20
	 If KeyDown(1) And gotim>35 Then go=-1
	 If KeyDown(28) Or JoyDown(1) 
	  If foc=<3 Then go=1
	  If foc=4 Then go=-1 
	 EndIf
	EndIf
	
	;CONFIGURATION
	If keytim=0
	 ;select slot
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=6 : PlaySound sMenuSelect
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=6 : PlaySound sMenuSelect
	 If foc>4 Then foc=1
	 If foc<1 Then foc=4
	 ;delete game
	 If (KeyDown(14) Or KeyDown(211)) And foc=<3
	  PlaySound sTrash : keytim=10
	  gamDay(foc)=0 : SaveProgressData()
	 EndIf
	EndIf 
	
	;scroll tile
	tileX#=tileX#+1.0
	tileY#=tileY#+0.25 
	
 UpdateWorld
 Next
 RenderWorld tween#

 ;DISPLAY
 TileImage gTile,tileX#,tileY#
 DrawImage gMDickie,rX#(120),rY#(535)
 DrawImage gMDickie,rX#(680),rY#(535) 
 ;previews
 y=270
 For cyc=1 To 3
  If foc=cyc And gamDay(cyc)>0 Then DrawImage gPreview(cyc),rX#(225),rY#(y)
  y=y+90
 Next
 DrawImage gLogo(1),rX#(400),rY#(140)
 ;options
 y=270
 For cyc=1 To 3
  SetFont fontX(2)
  namer$="START NEW GAME"
  If gamDay(cyc)>0 Then namer$="CONTINUE DAY "+gamDay(cyc)
  DrawMenu(cyc,400,y,namer$)
  SetFont fontX(3)
  If currRes=<1 Then x=285
  If currRes=2 Then x=308
  If currRes=>3 Then x=325
  If foc=cyc Then Outline(cyc,rX#(x),rY#(y),0,0,0,150,125,90)
  If foc<>cyc Then Outline(cyc,rX#(x),rY#(y),0,0,0,140,115,80)
  y=y+90
 Next
 SetFont fontX(2)
 DrawMenu(4,400,540,"<<< BACK <<<")

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
;proceed
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
If go=1 Then slot=foc : RestoreGame() : screen=10   
If go=-1 Then screen=1
End Function

;---------------------------------------------------------------------
;////////////////////// 6. DEATHMATCH SET-UP /////////////////////////
;---------------------------------------------------------------------
Function Deathmatch()
;frame rating
period=1000/FPS
time=MilliSecs()-period
;MAIN LOOP
slot=0 : foc=1 : oldfoc=foc
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
	If gotim>20 And keytim=0
	 If KeyDown(1) Then go=-1
	 If KeyDown(28) Or JoyDown(1)
	  If foc=5 And keytim=0 Then go=1
	  If foc=6 And keytim=0 Then go=-1 
	  If foc=<4 Then foc=5 : keytim=10 : PlaySound sMenuSelect
	 EndIf
	EndIf
	
	;CONFIGURATION
	If keytim=0
	 ;highlight option
	 If KeyDown(200) Or JoyYDir()=-1 Then foc=foc-1 : keytim=6 : PlaySound sMenuSelect
	 If KeyDown(208) Or JoyYDir()=1 Then foc=foc+1 : keytim=6 : PlaySound sMenuSelect
	 If foc>6 Then foc=1
	 If foc<1 Then foc=6 
	 ;browse left
	 If KeyDown(203) Or JoyXDir()=-1 
	  PlaySound sMenuBrowse : keytim=8
	  If foc=1 Then optIsland=optIsland-1
	  If foc=2 Then optPlays=optPlays-1 : keytim=5
	  If foc=3 Then optCast=optCast-1
	  If foc=4 Then optMulti=optMulti-1
	 EndIf
	 ;browse right
	 If KeyDown(205) Or JoyXDir()=1 
	  PlaySound sMenuBrowse : keytim=8
	  If foc=1 Then optIsland=optIsland+1
	  If foc=2 Then optPlays=optPlays+1 : keytim=5
	  If foc=3 Then optCast=optCast+1
	  If foc=4 Then optMulti=optMulti+1
	 EndIf
	EndIf
	;limits
	If optIsland<0 Then optIsland=5
	If optIsland>5 Then optIsland=0
	If optPlays<2 Then optPlays=2
	If optPlays>20 Then optPlays=20
	If optCast<0 Then optCast=2
	If optCast>2 Then optCast=0   
	If optMulti<0 Then optMulti=1
	If optMulti>1 Then optMulti=0  
	
	;scroll tile
	tileX#=tileX#+1.0
	tileY#=tileY#+0.25 
	
 UpdateWorld
 Next
 RenderWorld tween#

 ;DISPLAY
 TileImage gTile,tileX#,tileY#
 ;map diagram
 DrawImage gMap,rX#(400),rY#(150)
 If optIsland=1 Then DrawImage gIndicator,rX#(400),rY#(80)
 If optIsland=2 Then DrawImage gIndicator,rX#(475),rY#(145)
 If optIsland=3 Then DrawImage gIndicator,rX#(400),rY#(208)
 If optIsland=4 Then DrawImage gIndicator,rX#(325),rY#(145)
 If optIsland=5 Then DrawImage gIndicator,rX#(402),rY#(145)
 ;logos
 DrawImage gLogo(2),rX#(400),rY#(45)
 DrawImage gMDickie,rX#(120),rY#(535)
 DrawImage gMDickie,rX#(680),rY#(535)
 ;options
 y=250
 If currRes=<1 Then x1=260 : x2=540
 If currRes=2 Then x1=280 : x2=520
 If currRes=>3 Then x1=305 : x2=495
 SetFont fontX(2)
 DrawMenu(1,x1,y,"Location") : DrawMenu(1,x2,y,textIsland$(optIsland))
 DrawMenu(2,x1,y+55,"Participants") : DrawMenu(2,x2,y+55,optPlays)
 DrawMenu(3,x1,y+110,"Selection") : DrawMenu(3,x2,y+110,textCast$(optCast))
 DrawMenu(4,x1,y+165,"Multiplayer") : DrawMenu(4,x2,y+165,textOnOff$(optMulti))
 DrawMenu(5,400,480,">>> PROCEED >>>") 
 DrawMenu(6,400,540,"<<< SAVE & EXIT <<<")

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then ScreenShot()

Wend
;proceed
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
If go=1 Then RestoreGame() : screen=10
If go=-1 Then screen=1
End Function

;/////////////////////////////////////////////////////////////////////////////////////
;------------------------------ RELATED FUNCTIONS ------------------------------------
;/////////////////////////////////////////////////////////////////////////////////////

;ASSIGN KEY
Function AssignKey()
 value=0
 While value=0
  For k=0 To 255
   If KeyDown(k)=1 And k<>0 And k<>28 And k<>1 And k<>200 And k<>208 And k<>203 And k<>205 
    value=k : PlaySound sMenuSelect
   EndIf
  Next
 Wend
 Return value
End Function

;ASSIGN BUTTON
Function AssignButton()
 value=28
 While value=28
  For k=1 To 8
   If JoyDown(k)=1 Then value=k : PlaySound sMenuSelect
   If KeyDown(14) Then value=0 : PlaySound sMenuBack
  Next
 Wend
 Return value
End Function

;DRAW MENU ITEM
Function DrawMenu(box,x,y,script$)
 If (foc>0 And foc=box) Or box=-1
  ;active
  DrawImage gMenuA,rX#(x),rY#(y)
  Outline(script$,rX#(x),rY#(y)+1,0,0,0,150,125,90)
 Else
  ;inactive
  DrawImage gMenuB,rX#(x),rY#(y)
  Outline(script$,rX#(x),rY#(y)+1,0,0,0,140,115,80)
 EndIf
End Function

;LOADING DISPLAY
Function Loader(scriptA$,scriptB$,level)
 ;intro dislay
 If level=-1
  DrawImage gLogo(1),rX#(400),rY#(250)
  SetFont fontX(2)
  DrawMenu(0,400,550,scriptB$)
  SetFont fontX(3)
  DrawMenu(-1,400,500,scriptA$)
 EndIf
 ;in-game dislay
 If level=>0
  Cls
  ;logo
  TileImage gTile,tileX#,tileY#
  DrawImage gLogo(1),rX#(400),rY#(100)
  DrawImage gMDickie,rX#(120),rY#(535)
  DrawImage gMDickie,rX#(680),rY#(535)
  ;messages
  SetFont fontX(2)
  DrawMenu(0,400,525,scriptB$)
  SetFont fontX(3)
  DrawMenu(-1,400,475,scriptA$)
  ;map diagram
  DrawImage gMap,rX#(400),rY#(300)
  If gamJourney>0 And level>0 
   DrawImage gJourney(gamJourney,level),rX#(travelX#),rY#(travelY#)
  EndIf
  ;indicator
  If gamJourney=0 And level>0
   If gamIsland=1 Then DrawImage gIndicator,rX#(400),rY#(230)
   If gamIsland=2 Then DrawImage gIndicator,rX#(475),rY#(295)
   If gamIsland=3 Then DrawImage gIndicator,rX#(400),rY#(358)
   If gamIsland=4 Then DrawImage gIndicator,rX#(325),rY#(295)
   If gamIsland=5 Then DrawImage gIndicator,rX#(402),rY#(295)
  EndIf
 EndIf
 Flip
End Function

;CHANGE RESOLUTION
Function ChangeResolution()
 ;new graphical mode
 Loader("Please Wait","Adjusting Resolution",0)
 If optRes=0 Then Graphics3D 320,240,16,1
 If optRes=1 Then Graphics3D 640,480,16,1
 If optRes=2 Then Graphics3D 800,600,16,1
 If optRes=3 Then Graphics3D 1024,768,16,1
 If optRes=4 Then Graphics3D 1280,1024,16,1
 currRes=optRes
 ;restore media
 LoadImages()
 LoadTextures()
End Function

;GET SCREENSHOT
Function ScreenShot()   
 PlaySound sCamera
 screenshot=CreateImage(GraphicsWidth(),GraphicsHeight())
 GrabImage screenshot,GraphicsWidth()/2,GraphicsHeight()/2
 temp=MilliSecs()/10
 If screen<>10 Then namer$="Menu"
 If screen=10 And game=0 Then namer$="Deathmatch"
 If screen=10 And game=>1 Then namer$="Adventure"
 SaveImage(screenshot,"Photo Album/"+namer$+" - "+temp+".bmp")
End Function