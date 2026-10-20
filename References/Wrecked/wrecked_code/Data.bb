;//////////////////////////////////////////////////////////////////////////////
;----------------------------- WRECKED: DATA ----------------------------------
;//////////////////////////////////////////////////////////////////////////////

;MASTER PROCESS
Function RestoreGame()
 ;restore correct data
 loader=0
 If game=1 And slot>0 And gamDay(slot)>0 Then loader=slot
 LoadWorldData(loader)
 LoadPlayerData(loader)
 LoadResourceData(loader)
 LoadItemData(loader)
 DataOverride()
 ;reset new game
 If game=1 And slot>0 And gamDay(slot)=0 
  ResetProgressData(slot)
  camType=camDefault
  camX#(1)=-500 : camY#(1)=50 : camZ#(1)=600
  pHealth(1)=Rnd(60,100) : pDT(1)=270
  pHunger(1)=Rnd(5,40) : pThirst(1)=Rnd(5,50)
  For cyc=1 To no_plays
   pCostume(cyc)=0 : pHair(cyc)=0 : pFace(cyc)=0
   pBody(cyc)=0 : pArms(cyc)=0 : pLegs(cyc)=0
   If KeyDown(46) Then pCostume(cyc)=Rnd(1,21)
  Next
  If KeyDown(46) Then PlaySound sSink
 EndIf
 ;prepare deathmatch
 If game=0
  gamTime(slot)=Rnd(0,9000)
  gamIsland=optIsland
  If gamIsland=0 Then gamIsland=Rnd(1,5)
  For cyc=1 To no_plays
   If cyc=<optPlays 
    pIsland(cyc)=gamIsland : pState(cyc)=1
   Else 
    pIsland(cyc)=0 : pState(cyc)=0
   EndIf
   randy=Rnd(-2,3)
   If optCast=1 Then randy=1
   If optCast=2 Then randy=Rnd(2,3)
   If randy=<1 Then pType(cyc)=4
   If randy=2 Then pType(cyc)=10
   If randy=3 Then pType(cyc)=11 
   pHealth(cyc)=charHP(pType(cyc))*5
   pHP(cyc)=charHP(pType(cyc))
   pHunger(cyc)=Rnd(10,50)
   pThirst(cyc)=Rnd(10,50)
   pCarryA(cyc)=0 : pCarryB(cyc)=0
   pX#(cyc)=0 : pZ#(cyc)=0
  Next
  camX#(1)=0 : camY#(1)=100 : camZ#(1)=0
 EndIf
End Function

;-------------------------------------------------------
;//////////////////// OPTION DATA //////////////////////
;-------------------------------------------------------

;SAVE OPTION DATA
Function SaveOptionData()
 fileout=WriteFile("Data/Options.dat")
  ;preferences
  WriteInt fileout,optRes
  WriteInt fileout,optDisplay
  WriteInt fileout,optMusic
  WriteInt fileout,optAtmos
  WriteInt fileout,optFX
  WriteInt fileout,optBlood
  ;keys
  WriteInt fileout,keyAttackA
  WriteInt fileout,keyAttackB
  WriteInt fileout,keyPickA
  WriteInt fileout,keyPickB
  WriteInt fileout,keyJump
  WriteInt fileout,keySleep
  ;gamepad
  WriteInt fileout,buttAttackA
  WriteInt fileout,buttAttackB
  WriteInt fileout,buttPickA
  WriteInt fileout,buttPickB
  WriteInt fileout,buttJump
  WriteInt fileout,buttSleep
 CloseFile(fileout)
End Function

;LOAD OPTION DATA
Function LoadOptionData()
 filein=ReadFile("Data/Options.dat")
  ;preferences
  optRes=ReadInt(filein)
  optDisplay=ReadInt(filein)
  optMusic=ReadInt(filein)
  optAtmos=ReadInt(filein)
  optFX=ReadInt(filein)
  optBlood=ReadInt(filein)
  ;keys
  keyAttackA=ReadInt(filein)
  keyAttackB=ReadInt(filein)
  keyPickA=ReadInt(filein)
  keyPickB=ReadInt(filein)
  keyJump=ReadInt(filein)
  keySleep=ReadInt(filein)
  ;gamepad
  buttAttackA=ReadInt(filein)
  buttAttackB=ReadInt(filein)
  buttPickA=ReadInt(filein)
  buttPickB=ReadInt(filein)
  buttJump=ReadInt(filein)
  buttSleep=ReadInt(filein)
 CloseFile(filein)
End Function

;-------------------------------------------------------
;/////////////////// PROGRESS DATA /////////////////////
;-------------------------------------------------------

;RESET PROGRESS DATA
Function ResetProgressData(cyc)
 ;timeframe
 gamTime(cyc)=3000
 gamDay(cyc)=1
 ;spiritual state
 gamSoul(cyc)=3
 For count=1 To 20
  gamSins(cyc,count)=0
 Next
 gamCrime(cyc)=0
 ;missions
 gamMission(cyc)=0
 gamMissionDay(cyc)=0
 For count=1 To 20
  gamResult(cyc,count)=0
 Next
 ;checkpoints 
 For count=1 To 10
  gamCheckpoint(cyc,count)=0
 Next 
End Function

;SAVE PROGRESS DATA
Function SaveProgressData()
 fileout=WriteFile("Data/Progress.dat")
  For cyc=1 To 3
   ;timeframe
   WriteInt fileout,gamTime(cyc)
   WriteInt fileout,gamDay(cyc)
   ;spiritual state
   WriteInt fileout,gamSoul(cyc)
   For count=1 To 20
    WriteInt fileout,gamSins(cyc,count)
   Next
   WriteInt fileout,gamCrime(cyc)
   ;missions
   WriteInt fileout,gamMission(cyc)
   WriteInt fileout,gamMissionDay(cyc)
   For count=1 To 20
    WriteInt fileout,gamResult(cyc,count)
   Next
   ;checkpoints 
   For count=1 To 10
    WriteInt fileout,gamCheckpoint(cyc,count)
   Next 
  Next
 CloseFile(fileout)
End Function

;LOAD PROGRESS DATA
Function LoadProgressData()
 filein=ReadFile("Data/Progress.dat")
  For cyc=1 To 3
   ;timeframe
   gamTime(cyc)=ReadInt(filein)
   gamDay(cyc)=ReadInt(filein)
   ;spiritual state
   gamSoul(cyc)=ReadInt(filein)
   For count=1 To 20
    gamSins(cyc,count)=ReadInt(filein)
   Next
   gamCrime(cyc)=ReadInt(filein)
   ;missions
   gamMission(cyc)=ReadInt(filein)
   gamMissionDay(cyc)=ReadInt(filein)
   For count=1 To 20
    gamResult(cyc,count)=ReadInt(filein)
   Next
   ;checkpoints 
   For count=1 To 10
    gamCheckpoint(cyc,count)=ReadInt(filein)
   Next 
  Next
 CloseFile(filein)
End Function

;-------------------------------------------------------
;//////////////////// WORLD DATA ///////////////////////
;-------------------------------------------------------

;SAVE WORLD DATA
Function SaveWorldData(file)
 If file>0 Then gamFile$="Save0"+file Else gamFile$="Default"
 fileout=WriteFile("Data/"+gamFile$+"/World.dat")
  ;camera
  WriteInt fileout,camType
  WriteInt fileout,camX#(1)
  WriteInt fileout,camY#(1)
  WriteInt fileout,camZ#(1)
  WriteInt fileout,camTX#(1)
  WriteInt fileout,camTY#(1)
  WriteInt fileout,camTZ#(1)
  ;atmosphere
  WriteInt fileout,ambR#
  WriteInt fileout,ambG#
  WriteInt fileout,ambB#
  WriteInt fileout,lightR#
  WriteInt fileout,lightG#
  WriteInt fileout,lightB#  
  WriteInt fileout,atmosR#
  WriteInt fileout,atmosG#
  WriteInt fileout,atmosB#
  WriteInt fileout,skyR#
  WriteInt fileout,skyG#
  WriteInt fileout,skyB#
  ;additional
  For cyc=1 To 5
   ;fires
   WriteInt fileout,fireState(cyc)
   WriteInt fileout,fireSource(cyc)
   WriteInt fileout,fireX#(cyc)
   WriteInt fileout,fireY#(cyc)
   WriteInt fileout,fireZ#(cyc)
   WriteInt fileout,fireTim(cyc)
   ;boats
   WriteInt fileout,boatState(cyc)
   WriteInt fileout,boatX#(cyc)
   WriteInt fileout,boatY#(cyc)
   WriteInt fileout,boatZ#(cyc)
   WriteInt fileout,boatA#(cyc)
  Next
 CloseFile(fileout)
End Function

;LOAD WORLD DATA
Function LoadWorldData(file)
 If file>0 Then gamFile$="Save0"+file Else gamFile$="Default"
 filein=ReadFile("Data/"+gamFile$+"/World.dat")
  ;camera
  camType=ReadInt(filein)
  camX#(1)=ReadInt(filein)
  camY#(1)=ReadInt(filein)
  camZ#(1)=ReadInt(filein)
  camTX#(1)=ReadInt(filein)
  camTY#(1)=ReadInt(filein)
  camTZ#(1)=ReadInt(filein)
  ;atmosphere
  ambR#=ReadInt(filein)
  ambG#=ReadInt(filein)
  ambB#=ReadInt(filein)
  lightR#=ReadInt(filein)
  lightG#=ReadInt(filein)
  lightB#=ReadInt(filein)
  atmosR#=ReadInt(filein)
  atmosG#=ReadInt(filein)
  atmosB#=ReadInt(filein)
  skyR#=ReadInt(filein)
  skyG#=ReadInt(filein)
  skyB#=ReadInt(filein)
  ;additional
  For cyc=1 To 5
   ;fires
   fireState(cyc)=ReadInt(filein)
   fireSource(cyc)=ReadInt(filein)
   fireX#(cyc)=ReadInt(filein)
   fireY#(cyc)=ReadInt(filein)
   fireZ#(cyc)=ReadInt(filein)
   fireTim(cyc)=ReadInt(filein)
   ;boats
   boatState(cyc)=ReadInt(filein)
   boatX#(cyc)=ReadInt(filein)
   boatY#(cyc)=ReadInt(filein)
   boatZ#(cyc)=ReadInt(filein)
   boatA#(cyc)=ReadInt(filein)
  Next
 CloseFile(filein)
End Function

;-------------------------------------------------------
;/////////////////// PLAYER DATA /////////////////////
;-------------------------------------------------------

;SAVE PLAYER DATA
Function SavePlayerData(file)
 If file>0 Then gamFile$="Save0"+file Else gamFile$="Default"
 fileout=WriteFile("Data/"+gamFile$+"/Characters.dat")
  For cyc=1 To no_plays
   ;standard
   WriteInt fileout,pIsland(cyc)
   WriteInt fileout,pX#(cyc)
   WriteInt fileout,pY#(cyc)
   WriteInt fileout,pZ#(cyc)
   WriteInt fileout,pA#(cyc)
   WriteInt fileout,pGravity#(cyc)
   WriteInt fileout,pAnim(cyc)
   ;AI
   WriteInt fileout,pAgenda(cyc)
   WriteInt fileout,pTarget(cyc)
   WriteInt fileout,pTX#(cyc)
   WriteInt fileout,pTZ#(cyc)
   WriteInt fileout,pTA#(cyc)
   ;status
   WriteInt fileout,pType(cyc)
   WriteInt fileout,pState(cyc)
   WriteInt fileout,pHunger(cyc)
   WriteInt fileout,pThirst(cyc)
   WriteInt fileout,pHealth(cyc)
   WriteInt fileout,pHP(cyc)
   WriteInt fileout,pDT(cyc)
   WriteInt fileout,pCarryA(cyc) 
   WriteInt fileout,pCarryB(cyc)
   ;appearance
   WriteInt fileout,pCostume(cyc)
   WriteInt fileout,pHair(cyc)
   WriteInt fileout,pFace(cyc)
   WriteInt fileout,pBody(cyc)
   WriteInt fileout,pArms(cyc)
   WriteInt fileout,pLegs(cyc)
   For limb=1 To 20
    WriteInt fileout,pScar(cyc,limb)
   Next
  Next
 CloseFile(fileout)
End Function

;LOAD PLAYER DATA
Function LoadPlayerData(file)
 If file>0 Then gamFile$="Save0"+file Else gamFile$="Default"
 filein=ReadFile("Data/"+gamFile$+"/Characters.dat")
  For cyc=1 To no_plays
   ;standard
   pIsland(cyc)=ReadInt(filein)
   pX#(cyc)=ReadInt(filein)
   pY#(cyc)=ReadInt(filein)
   pZ#(cyc)=ReadInt(filein)
   pA#(cyc)=ReadInt(filein)
   pGravity#(cyc)=ReadInt(filein)
   pAnim(cyc)=ReadInt(filein)
   ;AI
   pAgenda(cyc)=ReadInt(filein)
   pTarget(cyc)=ReadInt(filein)
   pTX#(cyc)=ReadInt(filein)
   pTZ#(cyc)=ReadInt(filein)
   pTA#(cyc)=ReadInt(filein)
   ;status
   pType(cyc)=ReadInt(filein)
   pState(cyc)=ReadInt(filein)
   pHunger(cyc)=ReadInt(filein)
   pThirst(cyc)=ReadInt(filein)
   pHealth(cyc)=ReadInt(filein)
   pHP(cyc)=ReadInt(filein)
   pDT(cyc)=ReadInt(filein)
   pCarryA(cyc)=ReadInt(filein) 
   pCarryB(cyc)=ReadInt(filein)
   ;appearance
   pCostume(cyc)=ReadInt(filein)
   pHair(cyc)=ReadInt(filein)
   pFace(cyc)=ReadInt(filein)
   pBody(cyc)=ReadInt(filein)
   pArms(cyc)=ReadInt(filein)
   pLegs(cyc)=ReadInt(filein)
   For limb=1 To 20
    pScar(cyc,limb)=ReadInt(filein)
   Next
  Next
 CloseFile(filein)
End Function

;-------------------------------------------------------
;/////////////////// RESOURCE DATA /////////////////////
;-------------------------------------------------------

;SAVE RESOURCE DATA
Function SaveResourceData(file)
 If file>0 Then gamFile$="Save0"+file Else gamFile$="Default"
 fileout=WriteFile("Data/"+gamFile$+"/Resources.dat")
  For cyc=1 To no_resources
   WriteInt fileout,resIsland(cyc)
   WriteInt fileout,resType(cyc)
   WriteInt fileout,resState(cyc)
   WriteInt fileout,resX#(cyc)
   WriteInt fileout,resY#(cyc)
   WriteInt fileout,resZ#(cyc)
   WriteInt fileout,resA#(cyc)
   WriteInt fileout,resHP(cyc)
  Next
 CloseFile(fileout)
End Function

;LOAD RESOURCE DATA
Function LoadResourceData(file)
 If file>0 Then gamFile$="Save0"+file Else gamFile$="Default"
 filein=ReadFile("Data/"+gamFile$+"/Resources.dat")
  For cyc=1 To no_resources
   resIsland(cyc)=ReadInt(filein)
   resType(cyc)=ReadInt(filein)
   resState(cyc)=ReadInt(filein)
   resX#(cyc)=ReadInt(filein)
   resY#(cyc)=ReadInt(filein)
   resZ#(cyc)=ReadInt(filein)
   resA#(cyc)=ReadInt(filein)
   resHP(cyc)=ReadInt(filein)
  Next
 CloseFile(filein)
End Function

;-------------------------------------------------------
;///////////////////// ITEM DATA ///////////////////////
;-------------------------------------------------------

;SAVE ITEM DATA
Function SaveItemData(file)
 If file>0 Then gamFile$="Save0"+file Else gamFile$="Default"
 fileout=WriteFile("Data/"+gamFile$+"/Items.dat")
  For cyc=1 To no_items
   WriteInt fileout,iIsland(cyc)
   WriteInt fileout,iType(cyc)
   WriteInt fileout,iState(cyc)
   WriteInt fileout,iX#(cyc)
   WriteInt fileout,iY#(cyc)
   WriteInt fileout,iZ#(cyc)
   WriteInt fileout,iA#(cyc)
   WriteInt fileout,iFlaming(cyc)
  Next
 CloseFile(fileout)
End Function

;LOAD ITEM DATA
Function LoadItemData(file)
 If file>0 Then gamFile$="Save0"+file Else gamFile$="Default"
 filein=ReadFile("Data/"+gamFile$+"/Items.dat")
  For cyc=1 To no_items
   iIsland(cyc)=ReadInt(filein)
   iType(cyc)=ReadInt(filein)
   iState(cyc)=ReadInt(filein)
   iX#(cyc)=ReadInt(filein)
   iY#(cyc)=ReadInt(filein)
   iZ#(cyc)=ReadInt(filein)
   iA#(cyc)=ReadInt(filein)
   iFlaming(cyc)=ReadInt(filein)
  Next
 CloseFile(filein)
End Function

;-------------------------------------------------------
;/////////////////// DATA FIDDLING /////////////////////
;-------------------------------------------------------

;DATA OVERRIDE!
Function DataOverride()
 ;RESOURCES
 ;initiate resources
 ;For cyc=1 To no_resources
  ;If cyc=<30 Then resIsland(cyc)=3
  ;If cyc=>31 And cyc=<60 Then resIsland(cyc)=4
  ;If cyc=>61 And cyc=<90 Then resIsland(cyc)=1
  ;If cyc=>91 And cyc=<120 Then resIsland(cyc)=2
  ;If cyc=>121 And cyc=<150 Then resIsland(cyc)=5
  ;resHP(cyc)=resStrength(resType(cyc))
  ;resState(cyc)=1
 ;Next
 ;determine types
 ;s=30
 ;resType(s+1)=1 : resType(s+2)=1 : resType(s+3)=1 : resType(s+4)=2 : resType(s+5)=2
 ;resType(s+6)=3 : resType(s+7)=3 : resType(s+8)=3 : resType(s+9)=3 : resType(s+10)=3
 ;resType(s+11)=4 : resType(s+12)=4 : resType(s+13)=5 : resType(s+14)=5 : resType(s+15)=5
 ;resType(s+16)=5 : resType(s+17)=6 : resType(s+18)=6 : resType(s+19)=7 : resType(s+20)=7
 ;resType(s+21)=7 : resType(s+22)=7 : resType(s+23)=8 : resType(s+24)=8 : resType(s+25)=8
 ;resType(s+26)=8 : resType(s+27)=3 : resType(s+28)=2 : resType(s+29)=2 : resType(s+30)=1
 ;ITEMS
 ;distribute items
 ;item=0
 ;For island=1 To 5
  ;For cyc=1 To 75
   ;item=item+1
   ;values
   ;iIsland(item)=island
   ;iState(item)=0
   ;materials
   ;If cyc=>1 And cyc=<8 Then iType(item)=1
   ;If cyc=>9 And cyc=<16 Then iType(item)=2
   ;If cyc=>17 And cyc=<24 Then iType(item)=3
   ;If cyc=>25 And cyc=<32 Then iType(item)=4
   ;If cyc=>33 And cyc=<40 Then iType(item)=5
   ;products
   ;If cyc=>41 And cyc=<44 Then iType(item)=10
   ;If cyc=>45 And cyc=<48 Then iType(item)=11
   ;If cyc=>49 And cyc=<52 Then iType(item)=12
   ;If cyc=>53 And cyc=<55 Then iType(item)=13
   ;If cyc=>56 And cyc=<59 Then iType(item)=14
   ;If cyc=>60 And cyc=<63 Then iType(item)=15
   ;If cyc=>64 And cyc=<68 Then iType(item)=18
   ;If cyc=>69 And cyc=<73 Then iType(item)=19
   ;If cyc=>74 And cyc=<75 Then iType(item)=20
   ;gun in Centre
   ;If island=5 And cyc=68 Then iType(item)=16 : iState(item)=1 : iY#(item)=100
   ;artefect in West
   ;If island=4 And cyc=68 Then iType(item)=17 : iState(item)=1 : iY#(item)=100
  ;Next
 ;Next
 ;CHARACTERS
 ;For cyc=1 To 60
  ;If cyc=>1 And cyc=<13 Then pIsland(cyc)=3 ;south
  ;If cyc=>14 And cyc=<26 Then pIsland(cyc)=4 ;west
  ;If cyc=>27 And cyc=<35 Then pIsland(cyc)=5 ;centre
  ;If cyc=>36 And cyc=<47 Then pIsland(cyc)=1 ;north
  ;If cyc=>48 And cyc=<60 Then pIsland(cyc)=2 ;east
 ;Next
 ;South Coast types
 ;s=1
 ;pType(s)=1 : pType(s+1)=20 : pType(s+2)=20 : pType(s+3)=20 : pType(s+4)=20
 ;pType(s+5)=12 : pType(s+6)=12 : pType(s+7)=13 : pType(s+8)=13 : pType(s+9)=10
 ;pType(s+10)=11 : pType(s+11)=4 : pType(s+12)=4
 ;West Coast types
 ;s=14
 ;pType(s)=20 : pType(s+1)=20 : pType(s+2)=20 : pType(s+3)=20 : pType(s+4)=13
 ;pType(s+5)=13 : pType(s+6)=13 : pType(s+7)=12 : pType(s+8)=12 : pType(s+9)=11
 ;pType(s+10)=11 : pType(s+11)=4 : pType(s+12)=4
 ;Centre types
 ;s=27
 ;pType(s)=12 : pType(s+1)=12 : pType(s+2)=13 : pType(s+3)=13 : pType(s+4)=13
 ;pType(s+5)=11 : pType(s+6)=2 : pType(s+7)=4 : pType(s+8)=4
 ;North Coast types
 ;s=36
 ;pType(s)=3 : pType(s+1)=20 : pType(s+2)=20 : pType(s+3)=20 : pType(s+4)=10
 ;pType(s+5)=10 : pType(s+6)=11 : pType(s+7)=12 : pType(s+8)=13 : pType(s+9)=13
 ;pType(s+10)=4 : pType(s+11)=4
 ;East Coast types
 ;s=48
 ;pType(s)=20 : pType(s+1)=20 : pType(s+2)=20 : pType(s+3)=20 : pType(s+4)=13
 ;pType(s+5)=10 : pType(s+6)=10 : pType(s+7)=11 : pType(s+8)=12 : pType(s+9)=12
 ;pType(s+10)=13 : pType(s+11)=4 : pType(s+12)=4
 ;reset states
 ;For cyc=1 To no_plays
  ;pHunger(cyc)=0 : pThirst(cyc)=0
  ;pHealth(cyc)=charHP(pType(cyc))*5
  ;pHP(cyc)=charHP(pType(cyc))
  ;pState(cyc)=1
  ;pCarryA(cyc)=0 : pCarryB(cyc)=0
  ;If pTarget(cyc)>no_items Then pTarget(cyc)=no_items
 ;Next
 ;POSITION NOVELTIES
 ;For cyc=1 To no_plays
  ;soldier w/ gun
  ;If pType(cyc)=2 
   ;pX#(cyc)=-286 : pY#(cyc)=21 : pZ#(cyc)=272 : pA#(cyc)=60
   ;For v=1 To no_items
    ;If iType(v)=16 Then iState(v)=-1 : pCarryB(cyc)=v
   ;Next
  ;EndIf
  ;dog in pen
  ;If pType(cyc)=11 And pIsland(cyc)=5 And pZ#(cyc)>500 
   ;pX#(cyc)=-216 : pY#(cyc)=40 : pZ#(cyc)=521 : pA#(cyc)=180
  ;EndIf
  ;mystic
  ;If pType(cyc)=3 Then pX#(cyc)=-386 : pY#(cyc)=101 : pZ#(cyc)=-614 : pA#(cyc)=300
 ;Next
End Function