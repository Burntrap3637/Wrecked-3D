;//////////////////////////////////////////////////////////////////////////////
;---------------------------- WRECKED: WORLD ----------------------------------
;//////////////////////////////////////////////////////////////////////////////

;///////////////// LOAD ISLAND SEGMENT ///////////////////////
Function LoadIsland()
 ;1. NORTH COAST
 If gamIsland=1
  world=LoadAnimMesh("World/Island/North.3ds")
  EntityType world,10,1
  wNorth#=600 : wSouth#=-700
  wEast#=620 : wWest#=-750
  wWaterNorth#=wNorth# : wWaterSouth#=300
  wWaterEast#=wEast# : wWaterWest#=wWest#
  wNorthLead=0 : wSouthLead=5
  wEastLead=2 : wWestLead=4
 EndIf
 ;2. EAST COAST
 If gamIsland=2
  world=LoadAnimMesh("World/Island/East.3ds")
  EntityType world,10,1
  wNorth#=680 : wSouth#=-660
  wEast#=550 : wWest#=-720
  wWaterNorth#=wNorth# : wWaterSouth#=wSouth#
  wWaterEast#=wEast# : wWaterWest#=200
  wNorthLead=1 : wSouthLead=3
  wEastLead=0 : wWestLead=5
 EndIf
 ;3. SOUTH COAST
 If gamIsland=3
  world=LoadAnimMesh("World/Island/South.3ds")
  EntityType world,10,1
  wNorth#=700 : wSouth#=-550
  wEast#=650 : wWest#=-650
  wWaterNorth#=-280 : wWaterSouth#=wSouth#
  wWaterEast#=wEast# : wWaterWest#=wWest#
  wNorthLead=5 : wSouthLead=0
  wEastLead=2 : wWestLead=4
 EndIf
 ;4. WEST COAST
 If gamIsland=4
  world=LoadAnimMesh("World/Island/West.3ds")
  EntityType world,10,1
  wNorth#=700 : wSouth#=-650
  wEast#=650 : wWest#=-600
  wWaterNorth#=wNorth# : wWaterSouth#=wSouth#
  wWaterEast#=-420 : wWaterWest#=wWest#
  wNorthLead=1 : wSouthLead=3
  wEastLead=5 : wWestLead=0
 EndIf
 ;5. CENTRE
 If gamIsland=5
  world=LoadAnimMesh("World/Island/Centre.3ds")
  EntityType world,10,1
  wNorth#=550 : wSouth#=-540
  wEast#=680 : wWest#=-680
  wWaterNorth#=0 : wWaterSouth#=0
  wWaterEast#=0 : wWaterWest#=0
  wNorthLead=1 : wSouthLead=3
  wEastLead=2 : wWestLead=4
  ;novelties
  EntityAlpha FindChild(world,"Obelisk"),0.8
  EntityShininess FindChild(world,"Obelisk"),1.0
 EndIf
 ;texture water
 If FindChild(world,"Water")>0
  EntityTexture FindChild(world,"Water"),tWater
  EntityAlpha FindChild(world,"Water"),0.8
  EntityType FindChild(world,"Water"),11,0
 EndIf
 ;hide blocks
 If FindChild(world,"Block")>0 
  EntityAlpha FindChild(world,"Block"),0
  If game=2 Then EntityType FindChild(world,"Block"),0
 EndIf
End Function

;//////////////////////// LOAD ATMOSPHERE ///////////////////////////
Function LoadAtmos()
 ;CAMERA(S)
 If game=0 And optMulti=1 And screen>0 Then no_cameras=2 Else no_cameras=1
 For cyc=1 To no_cameras
  ;camera entity
  cam(cyc)=CreateCamera()
  If no_cameras=1 Then CameraViewport cam(cyc),0,0,GraphicsWidth(),GraphicsHeight() 
  If no_cameras=2 And cyc=1 Then CameraViewport cam(cyc),rX#(0),rY#(120),rX#(400),rY#(360) 
  If no_cameras=2 And cyc=2 Then CameraViewport cam(cyc),rX#(400),rY#(120),rX#(400),rY#(360) 
  CameraRange cam(cyc),1,2200
  EntityType cam(cyc),20,0
  EntityRadius cam(cyc),2
  PositionEntity cam(cyc),camX#(1),camY#(1),camZ#(1)
  camFoc(cyc)=cyc
  ;atmospherics
  If optAtmos>0 And game=<1
   CameraFogMode cam(cyc),1
   CameraFogRange cam(cyc),250,1100 
   CameraFogColor cam(cyc),atmosR#,atmosG#,atmosB#
  EndIf
 Next
 ;additional
 If game=2 Then camType=14
 camPivot=CreatePivot()
 dummy=CreatePivot() 
 ;LIGHTING
 ;sky variations
 EntityColor FindChild(world,"Sky"),skyR#,skyG#,skyB#
 ;ambient light
 AmbientLight ambR#,ambG#,ambB#
 ;If optAtmos=4 Then light=CreateLight(1) Else light=CreateLight(3)
 light=CreateLight(1) 
 LightColor light,lightR#,lightG#,lightB#
 ;location
 lightPivot=CreatePivot()
 If gamIsland=2 Then PositionEntity light,200,200,200
 If gamIsland<>2 Then PositionEntity light,-200,200,-200
 PositionEntity lightPivot,0,20,0 
 PointEntity light,lightPivot
 ;WEATHER
 If optWeather=1
  ;rain walls
  For count=1 To 20
   rain(count)=LoadMesh("World/Sprites/Rainer.3ds")
   EntityTexture rain(count),tRain
   EntityAlpha rain(count),0.1
  Next
  ;x rain
  pos#=0
  For count=1 To 5
   PositionEntity rain(count),pos#,Rnd(-10,10),Rnd(-10,10)
   RotateEntity rain(count),0,90,0
   pos#=pos#-100
  Next
  pos#=100
  For count=6 To 10
   PositionEntity rain(count),pos#,Rnd(-10,10),Rnd(-10,10)
   RotateEntity rain(count),0,90,0
   pos#=pos#+100
  Next
  ;z rain
  pos#=0
  For count=11 To 15
   PositionEntity rain(count),Rnd(-10,10),Rnd(-10,10),pos#
   pos#=pos#-100
  Next
  pos#=100
  For count=16 To 20
   PositionEntity rain(count),Rnd(-10,10),Rnd(-10,10),pos#
   pos#=pos#+100
  Next
 EndIf
End Function

;//////////////////// MANAGE ATMOSPHERE //////////////////////
Function ManageAtmosphere()
 ;increment time
 gamTime(slot)=gamTime(slot)+1
 If gamTime(slot)>18000 Or (game=0 And gamTime(slot)>12000) 
  gamTime(slot)=0 : gamDay(slot)=gamDay(slot)+1
 EndIf
 ;dawn properties
 If gamTime(slot)=>0 And gamTime(slot)=<3000 
  ambTR#=170 : ambTG#=170 : ambTB#=170
  lightTR#=130 : lightTG#=130 : lightTB#=130
  atmosTR#=170 : atmosTG#=160 : atmosTB#=160
  skyTR#=210 : skyTG#=210 : skyTB#=210
 EndIf
 ;day properties
 If gamTime(slot)=>3000 And gamTime(slot)=<6000
  ambTR#=200 : ambTG#=200 : ambTB#=200
  lightTR#=200 : lightTG#=200 : lightTB#=200
  atmosTR#=200 : atmosTG#=200 : atmosTB#=240
  skyTR#=255 : skyTG#=255 : skyTB#=255
 EndIf
 ;dusk properties
 If gamTime(slot)=>6000 And gamTime(slot)=<9000
  ambTR#=170 : ambTG#=170 : ambTB#=170
  lightTR#=120 : lightTG#=120 : lightTB#=120
  atmosTR#=160 : atmosTG#=130 : atmosTB#=100
  skyTR#=200 : skyTG#=200 : skyTB#=200
 EndIf
 ;day-dusk bridge
 If gamTime(slot)=>5900 And gamTime(slot)=<6600 Then atmosTR#=200 : atmosTG#=200
 ;twilight properties
 If (gamTime(slot)=>9000 And gamTime(slot)=<12000) Or (gamTime(slot)=>15000 And gamTime(slot)=<18000)
  ambTR#=130 : ambTG#=130 : ambTB#=130
  lightTR#=60 : lightTG#=60 : lightTB#=60
  atmosTR#=100 : atmosTG#=90 : atmosTB#=120
  skyTR#=155 : skyTG#=155 : skyTB#=155
 EndIf
 ;dusk-twilight bridge
 If gamTime(slot)=>9000 And gamTime(slot)=<9300 Then atmosTG#=130 : atmosTB#=100
 If gamTime(slot)=>9300 And gamTime(slot)=<9600 Then atmosTG#=100 : atmosTB#=100 
 ;night properties
 If gamTime(slot)=>12000 And gamTime(slot)=<15000
  ambTR#=80 : ambTG#=80 : ambTB#=80
  lightTR#=40 : lightTG#=40 : lightTB#=40
  atmosTR#=40 : atmosTG#=40 : atmosTB#=70
  skyTR#=100 : skyTG#=100 : skyTB#=100
 EndIf
 ;apply ambience
 changer#=0.1
 If ambR#<ambTR# Then ambR#=ambR#+changer#
 If ambR#>ambTR# Then ambR#=ambR#-changer#
 If ambG#<ambTG# Then ambG#=ambG#+changer#
 If ambG#>ambTG# Then ambG#=ambG#-changer#
 If ambB#<ambTB# Then ambB#=ambB#+changer#
 If ambB#>ambTB# Then ambB#=ambB#-changer# 
 AmbientLight ambR#,ambG#,ambB#
 ;apply lighting
 If lightR#<lightTR# Then lightR#=lightR#+changer#
 If lightR#>lightTR# Then lightR#=lightR#-changer#
 If lightG#<lightTG# Then lightG#=lightG#+changer#
 If lightG#>lightTG# Then lightG#=lightG#-changer#
 If lightB#<lightTB# Then lightB#=lightB#+changer#
 If lightB#>lightTB# Then lightB#=lightB#-changer# 
 LightColor light,lightR#,lightG#,lightB#
 ;apply atmosphere
 If atmosR#<atmosTR# Then atmosR#=atmosR#+changer#
 If atmosR#>atmosTR# Then atmosR#=atmosR#-changer#
 If atmosG#<atmosTG# Then atmosG#=atmosG#+changer#
 If atmosG#>atmosTG# Then atmosG#=atmosG#-changer#
 If atmosB#<atmosTB# Then atmosB#=atmosB#+changer#
 If atmosB#>atmosTB# Then atmosB#=atmosB#-changer# 
 If optAtmos>0 
  For cyc=1 To no_cameras
   CameraFogColor cam(cyc),atmosR#,atmosG#,atmosB#
  Next
 EndIf
 ;apply sky
 If skyR#<skyTR# Then skyR#=skyR#+changer#
 If skyR#>skyTR# Then skyR#=skyR#-changer#
 If skyG#<skyTG# Then skyG#=skyG#+changer#
 If skyG#>skyTG# Then skyG#=skyG#-changer#
 If skyB#<skyTB# Then skyB#=skyB#+changer#
 If skyB#>skyTB# Then skyB#=skyB#-changer# 
 EntityColor FindChild(world,"Sky"),skyR#,skyG#,skyB#
 ;rain flow
 If optWeather=1
  wRainFlow#=wRainFlow#+0.1
  PositionTexture tRain,0,wRainFlow#
 EndIf 
 ;water flow
 If FindChild(world,"Water")>0
  wWaterFlow#=wWaterFlow#-0.0005
  PositionTexture tWater,wWaterFlow#,0
 EndIf 
 ;obelisk turn
 If gamIsland=5 Then TurnEntity FindChild(world,"Obelisk"),0,0.5,0
End Function

;/////////////// CAMERA OPERATIONS /////////////////////////
Function Camera()
 ;CAMERA SELECTION
 If screen>0 And game=<1 And keytim=0
  ;manual override
  ;If KeyDown(23) Or KeyDown(36) Or KeyDown(37) Or KeyDown(38) Or KeyDown(21) Or KeyDown(35)
   ;camType=0 : camTim=50
  ;EndIf
  ;camera presets
  For count=2 To 8
   If KeyDown(count) Then camType=count-1 : camTim=200 : PlaySound sCamera : keytim=10
  Next
  If KeyDown(9) Then camType=11 : camTim=200 : PlaySound sCamera : keytim=10
  If KeyDown(10) Then camType=10 : camTim=200 : PlaySound sCamera : keytim=10
  If KeyDown(11) Then camType=0 : camTim=200 : PlaySound sCamera : keytim=10
 EndIf
 ;------------------ CAMERA CYCLE ------------------------------
 For cyc=1 To no_cameras
  ;honour collision effects
  camX#(cyc)=EntityX(cam(cyc))
  camY#(cyc)=EntityY(cam(cyc))
  camZ#(cyc)=EntityZ(cam(cyc))
  ;CAMERA TYPES
  ;manual control
  If camType=0
   If KeyDown(23) Then MoveEntity cam(cyc),0,0,1
   If KeyDown(37) Then MoveEntity cam(cyc),0,0,-1
   If KeyDown(36) Then MoveEntity cam(cyc),-1,0,0
   If KeyDown(38) Then MoveEntity cam(cyc),1,0,0
   If KeyDown(21) Then MoveEntity cam(cyc),0,1,0
   If KeyDown(35) Then MoveEntity cam(cyc),0,-1,0
   camX#(cyc)=EntityX(cam(cyc))
   camY#(cyc)=EntityY(cam(cyc))
   camZ#(cyc)=EntityZ(cam(cyc))
   camTX#(cyc)=camX#(cyc) : camTY#(cyc)=camY#(cyc) : camTZ#(cyc)=camZ#(cyc)
  EndIf
  ;first person
  If camType=1
   v=camFoc(cyc)
   PositionEntity dummy,EntityX(pLimb(v,1),1),EntityY(pLimb(v,1),1),EntityZ(pLimb(v,1),1)
   RotateEntity dummy,EntityPitch(pLimb(v,1),1),EntityYaw(pLimb(v,1),1),EntityRoll(pLimb(v,1),1)
   If pType(v)=10 Then RotateEntity dummy,0,EntityYaw(pLimb(v,1),1)+180,0
   If pType(v)=20 Then RotateEntity dummy,EntityPitch(pLimb(v,1),1),EntityYaw(pLimb(v,1),1)+270,EntityRoll(pLimb(v,1),1)
   If pType(v)=>10 And pType(v)=<12 Then MoveEntity dummy,0,3,6 Else MoveEntity dummy,0,3,3
   camX#(cyc)=EntityX(dummy) : camY#(cyc)=EntityY(dummy) : camZ#(cyc)=EntityZ(dummy)
  EndIf
  ;close follow
  If camType=2
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,0,0,-35
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+35
  EndIf
  ;raised follow
  If camType=3
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,0,0,-70
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+50
  EndIf
  ;distant follow
  If camType=4
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,0,0,-140
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+70
  EndIf
  ;high distant
  If camType=5
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,0,0,-150
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+150
  EndIf
  ;bird's eye
  If camType=6
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,0,0,-1
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+150
  EndIf
  ;static
  If camType=7
   camTX#(cyc)=0 : camTY#(cyc)=200 : camTZ#(cyc)=0
  EndIf
  ;NOVELTY CAMERAS
  ;promo headshot
  If camType=10
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,-10,0,25
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+30
   If pType(camFoc(cyc))=3 Then camTY#(cyc)=pY#(camFoc(cyc))+20
  EndIf
  ;death observer
  If camType=11
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,20,0,-50
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+20
  EndIf 
  ;heaven journey
  If camType=12
   camY#(cyc)=camY#(cyc)+0.5 : camTY#(cyc)=camY#(cyc)
   camTX#(cyc)=camX#(cyc) : camTZ#(cyc)=camZ#(cyc)
  EndIf    
  ;boat observer
  If camType=13
   camTX#(cyc)=-385 : camTY#(cyc)=120 : camTZ#(cyc)=-630
  EndIf
  ;ending headshot
  If camType=14
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,-30,0,40
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+20
  EndIf
  ;intro camera
  If camType=15
   ResetDummy(camFoc(cyc))
   MoveEntity dummy,20,0,-120
   camTX#(cyc)=EntityX(dummy) : camTZ#(cyc)=EntityZ(dummy)
   camTY#(cyc)=pY#(camFoc(cyc))+20
  EndIf
  ;dip under pier
  If gamIsland=2 And camType>0 
   v=camFoc(cyc)
   If pX#(v)>-300 And pX#(v)<180 And pZ#(v)>60 And pZ#(v)<150 And pY#(v)<20
    If camTY#(cyc)>20 Then camTY#(cyc)=20
   EndIf
  EndIf
  ;----------- OPERATION ----------------
  ;tracking 
  If camType<>1 And gotim>10
   camSpeed=15
   If screen=0 Or game=2 Then camSpeed=60
   If game=1 And gamCheckpoint(slot,4)=0 Then camSpeed=60
   GetCamSpeeds(cyc,camSpeed)
   If camX#(cyc)<camTX#(cyc) Then camX#(cyc)=camX#(cyc)+camSpeedX#
   If camX#(cyc)>camTX#(cyc) Then camX#(cyc)=camX#(cyc)-camSpeedX#
   If camY#(cyc)<camTY#(cyc) Then camY#(cyc)=camY#(cyc)+camSpeedY#
   If camY#(cyc)>camTY#(cyc) Then camY#(cyc)=camY#(cyc)-camSpeedY#
   If camZ#(cyc)<camTZ#(cyc) Then camZ#(cyc)=camZ#(cyc)+camSpeedZ#
   If camZ#(cyc)>camTZ#(cyc) Then camZ#(cyc)=camZ#(cyc)-camSpeedZ#
  EndIf
  ;world limits
  If camType<>1 And game=<1
   If camX#(cyc)<wWest# Then camX#(cyc)=wWest#
   If camX#(cyc)>wEast# Then camX#(cyc)=wEast#
   If camZ#(cyc)<wSouth# Then camZ#(cyc)=wSouth#
   If camZ#(cyc)>wNorth# Then camZ#(cyc)=wNorth#
  EndIf
  ;position and aim
  PositionEntity cam(cyc),camX#(cyc),camY#(cyc),camZ#(cyc) 
  PositionEntity camPivot,pX#(camFoc(cyc)),pY#(camFoc(cyc))+25,pZ#(camFoc(cyc))
  If camType=1 ;first person
   MoveEntity dummy,0,0,30
   PositionEntity camPivot,EntityX(dummy),EntityY(dummy),EntityZ(dummy)
  EndIf
  If gamPlanting=1 ;resource planting
   PositionEntity camPivot,resX#(gamPlanter),resY#(gamPlanter)+25,resZ#(gamPlanter)
  EndIf
  If pType(camFoc(cyc))=3 Or game=2 ;mystic/ending
   PositionEntity camPivot,pX#(camFoc(cyc)),pY#(camFoc(cyc))+15,pZ#(camFoc(cyc)) 
  EndIf
  PointEntity cam(cyc),camPivot
  ;position fader
  If fadeAlpha#<fadeTarget# Then fadeAlpha#=fadeAlpha#+0.0025
  If fadeAlpha#>0 And no_cameras=1
   PositionEntity dummy,camX#(cyc),camY#(cyc),camZ#(cyc)
   RotateEntity dummy,EntityPitch(cam(cyc)),EntityYaw(cam(cyc)),EntityRoll(cam(cyc))
   MoveEntity dummy,0,0,3
   PositionEntity fader,EntityX(dummy),EntityY(dummy),EntityZ(dummy)
   EntityAlpha fader,fadeAlpha#
  EndIf
 Next
End Function

;ZOOM CAMERA TO TARGET
Function ZoomCamera(cyc)
 camX#(cyc)=camTX#(cyc)
 camY#(cyc)=camTY#(cyc)
 camZ#(cyc)=camTZ#(cyc)
End Function

;RESET DUMMY (in terms of target)
Function ResetDummy(cyc)
 PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity dummy,0,pA#(cyc),0
End Function

;CALCULATE SMOOTH CAMERA SPEEDS
Function GetCamSpeeds(cyc,factor)
 ;X travelling
 camDiffX#=GetDiff#(camX#(cyc),camTX#(cyc))
 camSpeedX#=camDiffX#/factor
 ;Y travelling (related to X)
 camDiffY#=GetDiff#(camY#(cyc),camTY#(cyc))
 camAnchor#=camDiffY#/camDiffX#
 camSpeedY#=camSpeedX#*camAnchor#
 ;Z travelling (related to X)
 camDiffZ#=GetDiff#(camZ#(cyc),camTZ#(cyc))
 camAnchor#=camDiffZ#/camDiffX#
 camSpeedZ#=camSpeedX#*camAnchor#
End Function