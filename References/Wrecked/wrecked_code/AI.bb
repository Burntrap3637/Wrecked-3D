;//////////////////////////////////////////////////////////////////////////////
;---------------------- WRECKED: ARTIFICIAL INTELLIGENCE ----------------------
;//////////////////////////////////////////////////////////////////////////////

;////////////////// GET INPUT ////////////////////////////////
Function GetInput(cyc)
 ;reset commands
 cUp(cyc)=0 : cDown(cyc)=0 : cLeft(cyc)=0 : cRight(cyc)=0
 cAttackA(cyc)=0 : cAttackB(cyc)=0 : cPickA(cyc)=0 : cPickB(cyc)=0 
 cJump(cyc)=0
 ;get keyboard input
 If (pControl(cyc)=1 Or pControl(cyc)=3) And promo=0 And gamPlanting=0 And gotim>20
  If KeyDown(200) Then cUp(cyc)=1
  If KeyDown(208) Then cDown(cyc)=1
  If KeyDown(203) Then cLeft(cyc)=1
  If KeyDown(205) Then cRight(cyc)=1
  If KeyDown(keyAttackA) Then cAttackA(cyc)=1
  If KeyDown(keyAttackB) Then cAttackB(cyc)=1
  If KeyDown(keyPickA) Then cPickA(cyc)=1
  If KeyDown(keyPickB) Then cPickB(cyc)=1
  If KeyDown(keyJump) Then cJump(cyc)=1
  If KeyDown(keySleep) Then cPickA(cyc)=1 : cPickB(cyc)=1 
 EndIf
 ;get gamepad input
 If (pControl(cyc)=2 Or pControl(cyc)=3) And promo=0 And gamPlanting=0 And gotim>20
  If JoyYDir()=-1 Then cUp(cyc)=1
  If JoyYDir()=1 Then cDown(cyc)=1
  If JoyXDir()=-1 Then cLeft(cyc)=1
  If JoyXDir()=1 Then cRight(cyc)=1
  If JoyDown(buttAttackA) Then cAttackA(cyc)=1
  If JoyDown(buttAttackB) Then cAttackB(cyc)=1
  If JoyDown(buttPickA) Then cPickA(cyc)=1
  If JoyDown(buttPickB) Then cPickB(cyc)=1
  If JoyDown(buttJump) Then cJump(cyc)=1
  If JoyDown(buttSleep) Then cPickA(cyc)=1 : cPickB(cyc)=1 
 EndIf
 ;get CPU input
 ArtificialIntelligence(cyc)
 ;translate
 declawed=0
 If promo>0 Or game=2 Then declawed=1
 If game=0 And gamCheckpoint(slot,5)=1 Then declawed=1
 If KeyDown(207) And pControl(cyc)=0 Then declawed=1
 If declawed=0 Then TranslateInput(cyc)
End Function

;///////////////////// ARTIFICIAL INTELLIGENCE //////////////////////////////
Function ArtificialIntelligence(cyc)
 If pControl(cyc)=0 And gotim>20
  ;--- DETERMINE AGENDA
  ;random switch
  randy=Rnd(-2,1000)
  If randy=<0 Then pAgenda(cyc)=0 : pTarget(cyc)=0
  If randy=1 Or (game=0 And randy=<1) Then pAgenda(cyc)=1 : pTarget(cyc)=0
  If pCarryA(cyc)=0 Or pCarryB(cyc)=0
   If randy=2 Then pAgenda(cyc)=2 : pTarget(cyc)=0
   If randy=3 Or (game=0 And randy=>4 And randy=<5) Then pAgenda(cyc)=3 : pTarget(cyc)=0
  EndIf
  ;respect fire
  randy=Rnd(0,50)
  If randy=0 And FireProximity(cyc,30) Then pNowhere(cyc)=99
  ;logic checks
  If pAgenda(cyc)=1 And charPower(pType(cyc))=0 Then pAgenda(cyc)=0
  If pAgenda(cyc)=2 And pType(cyc)=>10 Then pAgenda(cyc)=0
  If KeyDown(199) Then pAgenda(cyc)=1 : pTarget(cyc)=1
  If pType(cyc)=3 Or (pType(cyc)=2 And gamCheckpoint(slot,1)=0) Then pAgenda(cyc)=0
  If pType(cyc)=>20 Then pAgenda(cyc)=0
  ;--- EXPLORING AGENDA
  If pAgenda(cyc)=0
   pTarget(cyc)=0
   randy=Rnd(1,800)
   If pType(cyc)=3 Or (pType(cyc)=2 And gamCheckpoint(slot,1)=0) Then randy=99
   ;contemplate
   If randy=1
    pTA#(cyc)=Rnd(0,360)
    pTX#(cyc)=pX#(cyc) : pTZ#(cyc)=pZ#(cyc)
   EndIf
   ;explore
   If (randy=2 Or pType(cyc)=>20) And ReachDest(cyc)=1
    pNowhere(cyc)=99
   EndIf
  EndIf
  ;--- ATTACKING AGENDA
  If pAgenda(cyc)=1
   ;find victim
   If pTarget(cyc)=0
    its=0
    Repeat
     satisfied=1
     pTarget(cyc)=Rnd(1,no_plays)
     If VictimViable(cyc,pTarget(cyc))=0 Then satisfied=0
     its=its+1
     If its>50 Or KeyDown(207) Then pTarget(cyc)=0 : satisfied=1
    Until satisfied=1
   EndIf
   ;abandon victim
   If VictimViable(cyc,pTarget(cyc))=0 Then pTarget(cyc)=0 : pAgenda(cyc)=0
   If game=1 And pHealth(pTarget(cyc))<10 And pHP(pTarget(cyc))=<0 
    pTarget(cyc)=0 : pAgenda(cyc)=0
   EndIf
   ;If game=1 and pType(cyc)=>10 And pHP(pTarget(cyc))=<0 Then pTarget(cyc)=0 : pAgenda(cyc)=0
   ;pursue victim
   If pTarget(cyc)>0
    pTX#(cyc)=pX#(pTarget(cyc))
    pTZ#(cyc)=pZ#(pTarget(cyc))
   EndIf
  EndIf
  ;--- RESOURCE AGENDA
  If pAgenda(cyc)=2
   ;find target
   If pTarget(cyc)=0
    its=0
    Repeat
     satisfied=1
     pTarget(cyc)=Rnd(1,no_resources)
     If ResourceViable(cyc,pTarget(cyc))=0 Then satisfied=0
     its=its+1
     If its>50 Or KeyDown(207) Then pTarget(cyc)=0 : satisfied=1
    Until satisfied=1
   EndIf
   ;abandon target
   If ResourceViable(cyc,pTarget(cyc))=0 Then pTarget(cyc)=0 : pAgenda(cyc)=0
   ;pursue target
   If pTarget(cyc)>0
    pTX#(cyc)=resX#(pTarget(cyc))
    pTZ#(cyc)=resZ#(pTarget(cyc))
   EndIf
  EndIf
  ;--- GATHERING AGENDA
  If pAgenda(cyc)=3
   ;find target
   If pTarget(cyc)=0
    its=0
    Repeat
     satisfied=1
     pTarget(cyc)=Rnd(iRange1,iRange2)
     If pType(cyc)=2 And pCarryA(cyc)<>368 And pCarryB(cyc)<>368 And gamIsland=5
      pTarget(cyc)=368 ;solider obsess over gun
     EndIf
     If ItemViable(cyc,pTarget(cyc))=0 Then satisfied=0
     its=its+1
     If its>50 Or KeyDown(207) Then pTarget(cyc)=0 : satisfied=1
    Until satisfied=1
   EndIf
   ;abandon target
   If ItemViable(cyc,pTarget(cyc))=0 Then pTarget(cyc)=0 : pAgenda(cyc)=0
   ;pursue target
   If pTarget(cyc)>0
    pTX#(cyc)=iX#(pTarget(cyc))
    pTZ#(cyc)=iZ#(pTarget(cyc))
   EndIf
  EndIf
  ;--- RETHINK ANY AGENDA if getting nowhere
  If pNowhere(cyc)>10
   pAgenda(cyc)=0
   ;mammal dimensions
   If pType(cyc)<20 
    Repeat
     satisfied=1
     pTX#(cyc)=Rnd(wWest#,wEast#)
     pTZ#(cyc)=Rnd(wSouth#,wNorth#)
     If pIsland(cyc)=5 And cyc=42 Then pTX#(cyc)=Rnd(-380,-180) : pTZ#(cyc)=Rnd(470,500) ;guard dog!
     If gamIsland=1 Then pTZ#(cyc)=Rnd(wSouth#,0) ;cliff offset
     randy=Rnd(0,1)
     If randy=0 And gamIsland=2 Then pTX#(cyc)=Rnd(wWest#,0) ;east coast offset
     If pTX#(cyc)=>wWaterWest# And pTX#(cyc)=<wWaterEast# And pTZ#(cyc)=>wWaterSouth# And pTZ#(cyc)=<wWaterNorth#
      If charHeight(pType(cyc))<20 Then satisfied=0
     EndIf
    Until satisfied=1
   EndIf
   ;fish dimensions
   If pType(cyc)=>20 
    pTX#(cyc)=Rnd(wWaterWest#,wWaterEast#) 
    pTZ#(cyc)=Rnd(wWaterSouth#,wWaterNorth#)
   EndIf
   ;jump out of pen!
   If gamIsland=5 And pZ#(cyc)>470 And pA#(cyc)=>90 And pA#(cyc)=<270 And pType(cyc)<10
    cJump(cyc)=1
   EndIf
   pNowhere(cyc)=0
  EndIf
  ;COMPULSARY POSITIONS
  ;solider looking at code
  If cyc=33 And gamCheckpoint(slot,1)=0 Then pTX#(cyc)=-286 : pTZ#(cyc)=272
  ;mystic on the mount
  If cyc=36 Then pTX#(cyc)=-386 : pTZ#(cyc)=-614
  ;--- ACT ON AGENDA
  ;update target angle
  If ReachDest(cyc)=0 And pAnim(cyc)=1 And pType(cyc)<>3
   PositionEntity dummy,pTX#(cyc),pY#(cyc),pTZ#(cyc)
   PointEntity p(cyc),dummy
   pTA#(cyc)=CleanAngle#(EntityYaw(p(cyc))) 
   RotateEntity p(cyc),0,pA#(cyc),0 
  EndIf
  ;turn
  If GetDiff#(pA#(cyc),pTA#(cyc))>10
   If ReachAngle#(pA#(cyc),pTA#(cyc),1)>0 Then cLeft(cyc)=1
   If ReachAngle#(pA#(cyc),pTA#(cyc),1)<0 Then cRight(cyc)=1
  EndIf  
  ;advance
  If ReachDest(cyc)=0 Then cUp(cyc)=1
  ;ATTACKING ISSUES
  If pAnim(cyc)<10 And pType(cyc)<>3
   If EnemyProximity(cyc,25,0)>0
    ;dociles
    If charPower(pType(cyc))=0 And pAnim(cyc)=0 Then pNowhere(cyc)=99
    ;predators
    If charPower(pType(cyc))>0
     threat=EnemyInRange(cyc,charRange(pType(cyc)))
     If threat>0
      randy=Rnd(0,5*threat)
      If randy=0 Then cAttackA(cyc)=1 
      If randy=1 Then cAttackB(cyc)=1   
     EndIf
    EndIf
   EndIf
  EndIf
  ;ITEM ISSUES
  For v=iRange1 To iRange2
   If ItemViable(cyc,v) And ItemProximity(cyc,v,50) And iState(v)<>0
    ;attraction
    randy=Rnd(0,80)
    For count=1 To 5
     If v=300+(count*8) And gamCheckpoint(slot,3)=0 Then randy=99 ;avoid theft!
    Next
    If iType(v)=>11 And iType(v)=<17 Then randy=Rnd(0,50)
    If randy=0 And iType(v)<>19 And (pCarryA(cyc)=0 Or pCarryB(cyc)=0)
     pAgenda(cyc)=3 : pTarget(cyc)=v
    EndIf
    ;collect
    randy=Rnd(0,16)
    If iType(v)=>11 And iType(v)=<17 Then randy=Rnd(0,10)
    If iType(v)=19 Then randy=99
    For count=1 To 5
     If v=300+(count*8) And gamCheckpoint(slot,3)=0 Then randy=99 ;avoid theft!
    Next
    If iState(v)=1 And pRecipe(cyc,0)=0 And (randy=0 Or (pAgenda(cyc)=3 And pTarget(cyc)=v))
     If ItemProximity(cyc,v,20)
      If ItemInRange(cyc,v,8)
       If pCarryA(cyc)=0 Then cPickA(cyc)=1 : cPickB(cyc)=0
       If pCarryB(cyc)=0 Then cPickA(cyc)=0 : cPickB(cyc)=1
      EndIf
     EndIf
    EndIf
   EndIf
  Next
  ;random drop
  randy=Rnd(0,2000)
  If randy=1 And pCarryA(cyc)>0 And iType(pCarryA(cyc))<>16 Then cPickA(cyc)=1 : cPickB(cyc)=0
  If randy=2 And pCarryB(cyc)>0 And iType(pCarryB(cyc))<>16 Then cPickA(cyc)=0 : cPickB(cyc)=1
  ;attempt drink?
  If pType(cyc)<10 And pY#(cyc)<optSeaLevel#-5 
   randy=Rnd(0,350-(pThirst(cyc)*3))
   If randy=1 And pCarryA(cyc)=0 Then cPickA(cyc)=1 : cPickB(cyc)=0
   If randy=2 And pCarryB(cyc)=0 Then cPickA(cyc)=0 : cPickB(cyc)=1
  EndIf
  ;sleep
  If game=>1 And gamTime(slot)=>11000 And gamTime(slot)=<16000
   If pType(cyc)<10 And pAnim(cyc)<>37 And pHealth(cyc)<70 And pY#(cyc)>optSeaLevel#
    randy=Rnd(0,100)
    If randy=0 
     If EnemyProximity(cyc,35,0)=0 Then cPickA(cyc)=1 : cPickB(cyc)=1
    EndIf
   EndIf
  EndIf
 EndIf
End Function

;///////////////////// TRANSLATE INPUT /////////////////////////////////
Function TranslateInput(cyc)
 ;movement
 If pAnim(cyc)=0 And DirPressed(cyc)=1 Then ChangeAnim(cyc,1)
 ;HUMAN ACTIONS
 If pType(cyc)<10
  If pAnim(cyc)=<1
   ;picking up
   If cPickA(cyc)=1 And pCarryA(cyc)=0 Then ChangeAnim(cyc,10) ;left pick up
   If cPickB(cyc)=1 And pCarryB(cyc)=0 Then ChangeAnim(cyc,11) ;right pick up
   ;dropping
   If cPickA(cyc)=1 And pCarryA(cyc)>0 Then ChangeAnim(cyc,12) ;left drop
   If cPickB(cyc)=1 And pCarryB(cyc)>0 Then ChangeAnim(cyc,13) ;right drop
   ;left attacking gauntlet
   If cAttackA(cyc)=1 Or cAttackB(cyc)=1 Then threat=EnemyProximity(cyc,25,1)
   If cAttackA(cyc)=1 
    If threat=1 Or (pCarryA(cyc)=0 And threat=0) Then ChangeAnim(cyc,20) ;high attack
    If threat=2 Then ChangeAnim(cyc,22) ;low attack
    If threat=3 Then ChangeAnim(cyc,24) ;ground attack
    If pCarryA(cyc)>0 And (threat=0 Or iType(pCarryA(cyc))<10) Then ChangeAnim(cyc,14) ;throw
   EndIf
   ;right attacking gauntlet
   If cAttackB(cyc)=1 
    If threat=1 Or (pCarryB(cyc)=0 And threat=0) Then ChangeAnim(cyc,21) ;high attack
    If threat=2 Then ChangeAnim(cyc,23) ;low attack
    If threat=3 Then ChangeAnim(cyc,25) ;ground attack
    If pCarryB(cyc)>0 And (threat=0 Or iType(pCarryB(cyc))<10) Then ChangeAnim(cyc,15) ;throw
   EndIf
   ;jumping
   If cJump(cyc)=1 Then ChangeAnim(cyc,2)
   ;sleep
   If cPickA(cyc)=1 And cPickB(cyc)=1 Then ChangeAnim(cyc,36)
   ;man boat!
   If pType(cyc)=1 And BoatProximity(cyc,12) And boatState(gamIsland)=>9
    If cPickA(cyc)=1 Or cPickB(cyc)=1 Then ChangeAnim(cyc,5)
   EndIf
  EndIf
  ;flying actions
  If (pAnim(cyc)=3 Or (pAnim(cyc)=2 And pAnimTim(cyc)>15)) And pType(cyc)<10
   If cAttackA(cyc)=1 And pY#(cyc)>pGround#(cyc)+5 Then ChangeAnim(cyc,26) ;left flying attack
   If cAttackB(cyc)=1 And pY#(cyc)>pGround#(cyc)+5 Then ChangeAnim(cyc,27) ;right flying attack
  EndIf
 EndIf
 ;ANIMAL ACTIONS
 If pType(cyc)=>10
  If pAnim(cyc)=<1
   If (cAttackA(cyc)=1 Or cAttackB(cyc)=1) And charRange(pType(cyc))>0 Then ChangeAnim(cyc,23) ;attack
   If cJump(cyc)=1 And pType(cyc)=10 Then ChangeAnim(cyc,2) ;jump
  EndIf
 EndIf
End Function

;--------------------------------------------------------------
;////////////////// RELATED FUNCTIONS /////////////////////////
;--------------------------------------------------------------

;SWITCH CONTROL
Function SwitchControl(search) ;-1=backwards, 1=forwards
 ;find suitable target
 newbie=camFoc(1)
 Repeat
  newbie=newbie+search
  If newbie>no_plays Then newbie=1
  If newbie<1 Then newbie=no_plays
 Until pIsland(newbie)=gamIsland And pState(newbie)=1 And pType(newbie)<>3
 ;switch controls
 If newbie<>camFoc(1)
  pControl(camFoc(1))=0
  pControl(newbie)=3
  camFoc(1)=newbie
 EndIf
End Function

;DIRECTIONS PRESSED?
Function DirPressed(cyc)
 value=0
 If cUp(cyc)=1 Or cDown(cyc)=1 Or cLeft(cyc)=1 Or cRight(cyc)=1 Then value=1
 Return value
End Function

;ACTIONS PRESSED?
Function ActionPressed(cyc)
 value=0
 If cAttackA(cyc)=1 Or cAttackB(cyc)=1 Then value=1
 If cPickA(cyc)=1 Or cPickB(cyc)=1 Then value=1
 If cJump(cyc)=1 Then value=1
 Return value
End Function

;CPU REACHED DESTINATION?
Function ReachDest(cyc)
 value=0 : range=12
 If pAgenda(cyc)=3 Then range=8
 If pX#(cyc)>pTX#(cyc)-range And pX#(cyc)<pTX#(cyc)+range And pZ#(cyc)>pTZ#(cyc)-range And pZ#(cyc)<pTZ#(cyc)+range
  value=1
 EndIf
 Return value
End Function

;CHECK PLAYER IS SUITABLE VICTIM
Function VictimViable(cyc,v)
 viable=1
 ;If charPower(pType(cyc))=0 And charPower(pType(v))>0 Then viable=0
 ;If pType(cyc)=pType(v) And charPower(pType(v))>0 Then viable=0
 If pType(v)=3 Then viable=0
 If pType(cyc)=4 And pType(v)=1 And gamSoul(slot)=>2 Then viable=0 
 If pType(v)=20 And pType(cyc)=>10 Then viable=0 
 If pState(v)=0 Then viable=0
 If pIsland(v)<>gamIsland Then viable=0
 If v=cyc Or v=0 Then viable=0
 Return viable
End Function

;CHECK RESOURCE IS SUITABLE TARGET
Function ResourceViable(cyc,v) 
 viable=1
 If resState(v)=0 Then viable=0
 If resIsland(v)<>gamIsland Then viable=0
 If v=0 Then viable=0
 Return viable
End Function

;CHECK ITEM IS SUITABLE TARGET
Function ItemViable(cyc,v) 
 viable=1
 ;food preferences
 If pType(cyc)=10 Or pType(cyc)=11 ;carnivores
  If iType(v)<>4 And iType(v)<>18 Then viable=0
 EndIf
 If pType(cyc)=12 Or pType(cyc)=13 ;herbivores
  If iType(v)<>1 And iType(v)<>5 And iType(v)<>10 Then viable=0
 EndIf
 If pType(cyc)=20 Then viable=0
 ;resist stealing
 If pType(cyc)<10
  For count=1 To 5
   If v=300+(count*8) And gamCheckpoint(slot,3)=0 Then viable=0
  Next
 EndIf
 ;standard logic
 If iType(v)=19 Then viable=0
 If pCarryA(cyc)=v Or pCarryB(cyc)=v Then viable=0
 If iState(v)=0 Or iState(v)=2 Then viable=0
 If iIsland(v)<>gamIsland Then viable=0
 If v=0 Then viable=0
 Return viable
End Function

;CHECK ENEMY IS IN PROXIMITY
Function EnemyProximity(cyc,range,style) ;0=centre probe, 1=advanced probe
 value=0
 ;prepare probe
 If style=1
  PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
  RotateEntity dummy,0,pA#(cyc),0
  MoveEntity dummy,0,0,10
  checkX#=EntityX(dummy)
  checkZ#=EntityZ(dummy)
 Else
  checkX#=pX#(cyc)
  checkZ#=pZ#(cyc)
 EndIf
 ;check for enemies
 For v=1 To no_plays
  If pIsland(v)=gamIsland And pState(v)>0 And cyc<>v
   If checkX#>pX#(v)-range And checkX#<pX#(v)+range And checkZ#>pZ#(v)-range And checkZ#<pZ#(v)+range And pY#(cyc)>pY#(v)-20 And pY#(cyc)<pY#(v)+20
    If AttackViable(v)=3 
     If value=0 Then value=3
    Else
     value=1
     If charHeight(pType(v))<20 Then value=2
     If charHeight(pType(v))<10 Then value=3
    EndIf
    If pType(cyc)=>12 And value>0 And pType(v)=1 And gamSoul(slot)=3 Then value=0
   EndIf
  EndIf
 Next
 ;check for resources
 If value=0 And pType(cyc)<10
  For v=1 To no_resources
   width=(range/2)+resWidth(resType(v))
   height=resHeight(resType(v))
   If resIsland(v)=gamIsland  And resState(v)>0
    If checkX#>resX#(v)-width And checkX#<resX#(v)+width And checkZ#>resZ#(v)-width And checkZ#<resZ#(v)+width And pY#(cyc)>resY#(v)-height And pY#(cyc)<resY#(v)+height
     If height=<15 Then value=2 Else value=1
    EndIf
   EndIf
  Next
 EndIf
 ;check for items
 If value=0 And pType(cyc)<10
  For v=1 To no_items
   If iIsland(v)=gamIsland And iState(v)>0 And iType(v)=<3 
    If checkX#>iX#(v)-(range/2) And checkX#<iX#(v)+(range/2) And checkZ#>iZ#(v)-(range/2) And checkZ#<iZ#(v)+(range/2) And pY#(cyc)>iY#(v)-20 And pY#(cyc)<iY#(v)+20
     value=3
    EndIf
   EndIf
  Next
 EndIf
 Return value ;1=high, 2=low, 3=ground
End Function

;ENEMIES IN RANGE FOR CPU?
Function EnemyInRange(cyc,range)
 value=0
 ;initiate dummy
 PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity dummy,0,pA#(cyc),0
 ;advance through range...
 For depth=1 To range
  MoveEntity dummy,0,0,4
  checkX#=EntityX(dummy)
  checkZ#=EntityZ(dummy)
  ;find human matches
  For v=1 To no_plays
   width=10
   height=charHeight(pType(v))
   If cyc<>v And checkX#>pX#(v)-width And checkX#<pX#(v)+width And checkZ#>pZ#(v)-width And checkZ#<pZ#(v)+width And pY#(cyc)>pY#(v)-height And pY#(cyc)<pY#(v)+(height/3)
    If AttackViable(v)=1 Or (AttackViable(v)=3 And game=0) Then value=1
    If game=1
     If AttackViable(v)=3 And pHealth(v)>10 Then value=3
     If pType(cyc)=pType(v) Or (pType(cyc)=4 And pType(v)=1 And gamSoul(slot)=>2) Then value=value*4
    EndIf
   EndIf
  Next
  ;find resource matches
  If pType(cyc)<10
   For v=1 To no_resources
    width=resWidth(resType(v))
    height=resHeight(resType(v))
    If resIsland(v)=gamIsland And resState(v)>0 And checkX#>resX#(v)-width And checkX#<resX#(v)+width And checkZ#>resZ#(v)-width And checkZ#<resZ#(v)+width And pY#(cyc)>resY#(v)-25 And pY#(cyc)<resY#(v)+(height/2)
     value=2
    EndIf
   Next
   ;find material matches
   For v=1 To no_items
    If iIsland(v)=gamIsland And iState(v)>0 And iType(v)=<3 And checkX#>iX#(v)-8 And checkX#<iX#(v)+8 And checkZ#>iZ#(v)-8 And checkZ#<iZ#(v)+8 And pY#(cyc)>iY#(v)-10 And pY#(cyc)<iY#(v)+10
     value=3
    EndIf
   Next
  EndIf
 Next
 Return value ;1=human, 2=resource, 3=item
End Function

;CHECK ITEM IS IN PROXIMITY
Function ItemProximity(cyc,v,range)
 value=0
 ;check for item
 If iIsland(v)=gamIsland
  If pX#(cyc)>iX#(v)-range And pX#(cyc)<iX#(v)+range And pZ#(cyc)>iZ#(v)-range And pZ#(cyc)<iZ#(v)+range And pY#(cyc)>iY#(v)-10 And pY#(cyc)<iY#(v)+10
   value=1
  EndIf
 EndIf
 Return value
End Function

;CHECK ITEM IS IN RANGE
Function ItemInRange(cyc,v,range)
 value=0
 ;use advanced probe
 PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity dummy,0,pA#(cyc),0
 MoveEntity dummy,0,0,6
 checkX#=EntityX(dummy)
 checkZ#=EntityZ(dummy)
 ;check for item
 If iIsland(v)=gamIsland And iState(v)=1
  If checkX#>iX#(v)-range And checkX#<iX#(v)+range And checkZ#>iZ#(v)-range And checkZ#<iZ#(v)+range And pY#(cyc)>iY#(v)-10 And pY#(cyc)<iY#(v)+10
   value=1
  EndIf
 EndIf
 Return value
End Function