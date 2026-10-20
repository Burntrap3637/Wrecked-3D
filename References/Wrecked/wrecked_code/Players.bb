;//////////////////////////////////////////////////////////////////////////////
;----------------------------- WRECKED: PLAYERS -------------------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////// LOAD PLAYERS ///////////////////////////
Function LoadPlayers() 
 For cyc=1 To no_plays
  If pIsland(cyc)=gamIsland
   ;model & sequences
   p(cyc)=LoadAnimMesh("Characters/"+charName$(pType(cyc))+"/"+charName$(pType(cyc))+".3ds") 
   LoadSequences(cyc)
   ;costume issues
   GetLimbs(cyc)
   If pType(cyc)<10
    If pType(cyc)=1 And pCostume(cyc)=0 Then pCostume(cyc)=1
    If pType(cyc)=2 And pCostume(cyc)=0 Then pCostume(cyc)=2
    If pType(cyc)=3 And pCostume(cyc)=0 Then pCostume(cyc)=3
    If pType(cyc)=4 And pCostume(cyc)=0 Then pCostume(cyc)=Rnd(4,14) 
    If game=0 Then pCostume(cyc)=Rnd(1,21)
    ApplyCostume(cyc,pCostume(cyc)) 
   EndIf
   ;reset scars
   For limb=1 To 20
    pOldScar(cyc,limb)=0
   Next
   ;location
   If pX#(cyc)=0 And pZ#(cyc)=0 
    pX#(cyc)=Rnd(wWest#+100,wEast#-100)
    pZ#(cyc)=Rnd(wSouth#+100,wNorth#-100)
    pY#(cyc)=110 : pA#(cyc)=Rnd(0,360)
    If pType(cyc)=>20
     pTX#(cyc)=Rnd(wWaterWest#,wWaterEast#) 
     pTZ#(cyc)=Rnd(wWaterSouth#,wWaterNorth#)
     pY#(cyc)=10
    EndIf
   EndIf
   If pType(cyc)=3 ;stabilize mystic
    pX#(cyc)=-386 : pY#(cyc)=101 : pZ#(cyc)=-614 : pA#(cyc)=330
    If game=2 Then pAnim(cyc)=0 : pState(cyc)=1 : pHealth(cyc)=99 : pHP(cyc)=99
   EndIf
   pOldX#(cyc)=pX#(cyc) : pOldY#(cyc)=pY#(cyc) : pOldZ#(cyc)=pZ#(cyc)
   PositionEntity p(cyc),pX#(cyc),pY#(cyc)+3,pZ#(cyc)
   RotateEntity p(cyc),0,pA#(cyc),0
   ScaleEntity p(cyc),0.4,0.4,0.4
   ;default animation
   Animate p(cyc),1,0.3,pSeq(cyc,1),0
   If game=>1
    If pAnim(cyc)=5 Then Animate p(cyc),1,0.2,pSeq(cyc,139),0
    If pAnim(cyc)=33 Or pAnim(cyc)=34 Then Animate p(cyc),1,0.2,pSeq(cyc,134),0
    If pAnim(cyc)=37 Then Animate p(cyc),1,0.2,pSeq(cyc,137),0
    If game=0 Then Animate p(cyc),1,0.2,pSeq(cyc,122),0 : pAnim(cyc)=3
   EndIf
   ;collision detection
   pPivot(cyc)=CreatePivot()
   EntityType pPivot(cyc),1,0
   EntityRadius pPivot(cyc),charWidth(pType(cyc)),18
   PositionEntity pPivot(cyc),pX#(cyc),pY#(cyc)+3,pZ#(cyc)
   ;values
   If pAnim(cyc)=5 Then pAnimTim(cyc)=10 Else pAnimTim(cyc)=0
   pEyes(cyc)=Rnd(1,3) : pOldEyes(cyc)=0 : pMouth(cyc)=0
   ;determine controls
   pControl(cyc)=0
   If cyc=1 Then pControl(cyc)=3
   If game=0 And cyc=1 And optMulti=1 Then pControl(cyc)=1 
   If game=0 And cyc=2 And optMulti=1 Then pControl(cyc)=2 
   ;shadow
   pShadow(cyc)=LoadSprite("World/Sprites/Shadow.bmp",12)
   ScaleSprite pShadow(cyc),10,10
   If charWidth(pType(cyc))<8 Then ScaleSprite pShadow(cyc),charWidth(pType(cyc))+1,charWidth(pType(cyc))+1
   RotateEntity pShadow(cyc),90,0,0
   SpriteViewMode pShadow(cyc),2
   EntityAlpha pShadow(cyc),0.1
   PositionEntity pShadow(cyc),pX#(cyc),pY#(cyc)+0.4,pZ#(cyc)
  EndIf
 Next
End Function

;////////////// IDENTIFY LIMBS ////////////////////
Function GetLimbs(cyc)
 ;reset 
 For limb=1 To 20
  pLimb(cyc,limb)=0
 Next
 ;standard limbs
 pLimb(cyc,1)=FindChild(p(cyc),"Head")
 pLimb(cyc,2)=FindChild(p(cyc),"Neck")
 pLimb(cyc,3)=FindChild(p(cyc),"Body")
 pLimb(cyc,4)=FindChild(p(cyc),"L_Bicep")
 pLimb(cyc,5)=FindChild(p(cyc),"R_Bicep")
 pLimb(cyc,6)=FindChild(p(cyc),"L_Arm")
 pLimb(cyc,7)=FindChild(p(cyc),"R_Arm")
 pLimb(cyc,8)=FindChild(p(cyc),"L_Hand")
 pLimb(cyc,9)=FindChild(p(cyc),"R_Hand")
 pLimb(cyc,10)=FindChild(p(cyc),"Hips")
 pLimb(cyc,11)=FindChild(p(cyc),"L_Thigh")
 pLimb(cyc,12)=FindChild(p(cyc),"R_Thigh")
 pLimb(cyc,13)=FindChild(p(cyc),"L_Leg")
 pLimb(cyc,14)=FindChild(p(cyc),"R_Leg")
 pLimb(cyc,15)=FindChild(p(cyc),"L_Foot")
 pLimb(cyc,16)=FindChild(p(cyc),"R_Foot")
 ;leopard/wolf variations
 If pType(cyc)=10 Or pType(cyc)=11
  pLimb(cyc,3)=FindChild(p(cyc),"Torso")
  pLimb(cyc,8)=FindChild(p(cyc),"L_Paw")
  pLimb(cyc,9)=FindChild(p(cyc),"R_Paw")
  pLimb(cyc,17)=FindChild(p(cyc),"Tail01")
  pLimb(cyc,18)=FindChild(p(cyc),"Tail02")
  pLimb(cyc,19)=FindChild(p(cyc),"Tail03")
 EndIf
 ;deer variations
 If pType(cyc)=12
  pLimb(cyc,3)=FindChild(p(cyc),"Torso")
  pLimb(cyc,17)=FindChild(p(cyc),"Tail")
 EndIf
 ;rabbit variations
 If pType(cyc)=13
  pLimb(cyc,3)=FindChild(p(cyc),"Torso")
  pLimb(cyc,8)=FindChild(p(cyc),"L_Paw")
  pLimb(cyc,9)=FindChild(p(cyc),"R_Paw")
  pLimb(cyc,17)=FindChild(p(cyc),"Tail")
  pLimb(cyc,18)=FindChild(p(cyc),"L_Ear")
  pLimb(cyc,19)=FindChild(p(cyc),"R_Ear")
 EndIf
 ;fish variations
 If pType(cyc)=20
  pLimb(cyc,3)=FindChild(p(cyc),"Torso")
  pLimb(cyc,17)=FindChild(p(cyc),"Tail")
 EndIf
End Function

;////////////////////// APPLY COSTUME /////////////////////////
Function ApplyCostume(cyc,costume)
 ;hide hair variations
 HideEntity FindChild(p(cyc),"Helmet") : HideEntity FindChild(p(cyc),"Hat")
 HideEntity FindChild(p(cyc),"Hair_Short") : HideEntity FindChild(p(cyc),"Hair_Spike")
 HideEntity FindChild(p(cyc),"Hair_Quiff") : HideEntity FindChild(p(cyc),"Hair_Mop")
 HideEntity FindChild(p(cyc),"Hair_Afro") : HideEntity FindChild(p(cyc),"Hair_Full")
 HideEntity FindChild(p(cyc),"Hair_Punk") : HideEntity FindChild(p(cyc),"Hair_Bald")
 HideEntity FindChild(p(cyc),"Hair_Thin") : HideEntity FindChild(p(cyc),"Hair_Thick")
 HideEntity FindChild(p(cyc),"Hair_Pony")
 ;get costume preferences
 If costume=1 ;player
  pHair(cyc)=1 : pFace(cyc)=1 : pBody(cyc)=1 : pArms(cyc)=1 : pLegs(cyc)=1
  ShowEntity FindChild(p(cyc),"Hair_Full")
  EntityTexture FindChild(p(cyc),"Hair_Full"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=2 ;soldier
  pHair(cyc)=2 : pFace(cyc)=2 : pBody(cyc)=2 : pArms(cyc)=2 : pLegs(cyc)=2
  ShowEntity FindChild(p(cyc),"Helmet")
 EndIf
 If costume=3 ;mystic native
  pHair(cyc)=3 : pFace(cyc)=7 : pBody(cyc)=4 : pArms(cyc)=3 : pLegs(cyc)=3
  ShowEntity FindChild(p(cyc),"Hair_Full")
  EntityTexture FindChild(p(cyc),"Hair_Full"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=>4 And costume=<14 ;standard native costume
  If pHair(cyc)=0 Then pHair(cyc)=2
  If pFace(cyc)=0 Then pFace(cyc)=Rnd(3,6)
  If pBody(cyc)=0 Then pBody(cyc)=Rnd(3,4)
  If pArms(cyc)=0 Then pArms(cyc)=3
  If pLegs(cyc)=0 Then pLegs(cyc)=3 
 EndIf
 If costume=4 Or costume=13 Or costume=14 ;standard native
  ShowEntity FindChild(p(cyc),"Hair_Full")
  EntityTexture FindChild(p(cyc),"Hair_Full"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=5 ;bald native
 EndIf
 If costume=6 ;spikey native
  ShowEntity FindChild(p(cyc),"Hair_Spike")
  EntityTexture FindChild(p(cyc),"Hair_Spike"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=7 ;afro native
  ShowEntity FindChild(p(cyc),"Hair_Afro")
  EntityTexture FindChild(p(cyc),"Hair_Afro"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=>8 And costume=<12 ;ponytail variations
  ShowEntity FindChild(p(cyc),"Hair_Pony")
  EntityTexture FindChild(p(cyc),"Hair_Pony"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=8 ;short native
  ShowEntity FindChild(p(cyc),"Hair_Thin")
  EntityTexture FindChild(p(cyc),"Hair_Thin"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=9 ;mohican native
  ShowEntity FindChild(p(cyc),"Hair_Punk")
  EntityTexture FindChild(p(cyc),"Hair_Punk"),tHair(pHair(cyc)),0,1
 EndIf 
 If costume=10 ;fringe native
  ShowEntity FindChild(p(cyc),"Hair_Mop")
  EntityTexture FindChild(p(cyc),"Hair_Mop"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=11 ;thick native
  ShowEntity FindChild(p(cyc),"Hair_Thick")
  EntityTexture FindChild(p(cyc),"Hair_Thick"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=12 ;balding native
  ShowEntity FindChild(p(cyc),"Hair_Bald")
  EntityTexture FindChild(p(cyc),"Hair_Bald"),tHair(pHair(cyc)),0,1
 EndIf
 ;novelties!
 If costume=15 ;MDickie
  pHair(cyc)=4 : pFace(cyc)=8 : pBody(cyc)=5 : pArms(cyc)=4 : pLegs(cyc)=4
  ShowEntity FindChild(p(cyc),"Hat")
  ShowEntity FindChild(p(cyc),"Hair_Short")
  EntityTexture FindChild(p(cyc),"Hair_Short"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=16 ;Sure Shot
  pHair(cyc)=2 : pFace(cyc)=9 : pBody(cyc)=6 : pArms(cyc)=5 : pLegs(cyc)=5
  ShowEntity FindChild(p(cyc),"Helmet")
 EndIf
 If costume=17 ;knifeman
  pHair(cyc)=1 : pFace(cyc)=10 : pBody(cyc)=7 : pArms(cyc)=6 : pLegs(cyc)=6
  ShowEntity FindChild(p(cyc),"Hair_Thin")
  EntityTexture FindChild(p(cyc),"Hair_Thin"),tHair(pHair(cyc)),0,1
  ShowEntity FindChild(p(cyc),"Hair_Pony")
  EntityTexture FindChild(p(cyc),"Hair_Pony"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=18 ;muscleman
  pHair(cyc)=4 : pFace(cyc)=9 : pBody(cyc)=1 : pArms(cyc)=1 : pLegs(cyc)=6
 EndIf
 If costume=19 ;officer
  pHair(cyc)=4 : pFace(cyc)=11 : pBody(cyc)=8 : pArms(cyc)=7 : pLegs(cyc)=7
  ShowEntity FindChild(p(cyc),"Hair_Bald")
  EntityTexture FindChild(p(cyc),"Hair_Bald"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=20 ;rapper
  pHair(cyc)=2 : pFace(cyc)=12 : pBody(cyc)=9 : pArms(cyc)=8 : pLegs(cyc)=8
  ShowEntity FindChild(p(cyc),"Hair_Short")
  EntityTexture FindChild(p(cyc),"Hair_Short"),tHair(pHair(cyc)),0,1
 EndIf
 If costume=21 ;sixties
  pHair(cyc)=2 : pFace(cyc)=13 : pBody(cyc)=10 : pArms(cyc)=9 : pLegs(cyc)=9
  ShowEntity FindChild(p(cyc),"Hair_Afro")
  EntityTexture FindChild(p(cyc),"Hair_Afro"),tHair(pHair(cyc)),0,1
 EndIf
 ;apply costume
 EntityTexture FindChild(p(cyc),"Head"),tFace(pFace(cyc)),0,1
 EntityTexture FindChild(p(cyc),"Neck"),tFace(pFace(cyc)),0,1
 EntityTexture FindChild(p(cyc),"Body"),tBody(pBody(cyc)),0,1
 EntityTexture FindChild(p(cyc),"R_Bicep"),tArms(pArms(cyc)),0,1
 EntityTexture FindChild(p(cyc),"R_Arm"),tArms(pArms(cyc)),0,1
 EntityTexture FindChild(p(cyc),"R_Hand"),tArms(pArms(cyc)),0,1
 EntityTexture FindChild(p(cyc),"L_Bicep"),tArms(pArms(cyc)),0,1
 EntityTexture FindChild(p(cyc),"L_Arm"),tArms(pArms(cyc)),0,1
 EntityTexture FindChild(p(cyc),"L_Hand"),tArms(pArms(cyc)),0,1
 EntityTexture FindChild(p(cyc),"Hips"),tLegs(pLegs(cyc)),0,1
 EntityTexture FindChild(p(cyc),"R_Thigh"),tLegs(pLegs(cyc)),0,1
 EntityTexture FindChild(p(cyc),"R_Leg"),tLegs(pLegs(cyc)),0,1
 EntityTexture FindChild(p(cyc),"R_Foot"),tLegs(pLegs(cyc)),0,1
 EntityTexture FindChild(p(cyc),"L_Thigh"),tLegs(pLegs(cyc)),0,1
 EntityTexture FindChild(p(cyc),"L_Leg"),tLegs(pLegs(cyc)),0,1
 EntityTexture FindChild(p(cyc),"L_Foot"),tLegs(pLegs(cyc)),0,1
End Function

;///////////////////////// PLAYER CYCLE /////////////////////////////
Function PlayerCycle()
 For cyc=1 To no_plays
  If pIsland(cyc)=gamIsland
   If pState(cyc)>0
    ;honour collision detection
    pGround#(cyc)=EntityY(pShadow(cyc))
    pX#(cyc)=EntityX(pPivot(cyc))
    pY#(cyc)=EntityY(pPivot(cyc))-18
    pZ#(cyc)=EntityZ(pPivot(cyc))
    ;enforce blocks
    EnforceBlocks(cyc)
    ;check for input
    GetInput(cyc)
    ;trigger collapse
    If pHP(cyc)=<0 And CollapseViable(cyc)=1 Then ChangeAnim(cyc,32)
    ;trigger falling
    If pY#(cyc)>pGround#(cyc)+5 And FallViable(cyc)=1 And gotim>20 Then ChangeAnim(cyc,3)
    ;manage animations
    PlayerAnims(cyc)
    ;gravity
    If (pY#(cyc)=pOldY#(cyc) Or pY#(cyc)<-10) And pGravity#(cyc)<-1.0 Then pGravity#(cyc)=-1.0
    If pGravity#(cyc)>-6.0 Then pGravity#(cyc)=pGravity#(cyc)-0.2
    If gamCollisions=0 Then pGravity#(cyc)=0
    MoveEntity pPivot(cyc),0,pGravity#(cyc),0 
    If (cyc=1 Or KeyDown(56)) And KeyDown(201) Then MoveEntity pPivot(cyc),0,3,0 : pGravity#(cyc)=0
    ;hunger issues
    randy=Rnd(0,250)
    If pAnim(cyc)>1 And pAnim(cyc)<>5 And pAnim(cyc)<>33 And pAnim(cyc)<>37 Then randy=Rnd(0,25)
    If randy=0 Then pHunger(cyc)=pHunger(cyc)+1
    If pHunger(cyc)>100 Then pHunger(cyc)=100
    If pHunger(cyc)<0 Then pHunger(cyc)=0
    If randy=99 And pHunger(cyc)=>80 ;hunger pains
     ProduceSound(sPain(1),22050,0)
     If pHealth(cyc)>1 Then pHealth(cyc)=pHealth(cyc)-1
     pHP(cyc)=pHP(cyc)-1
    EndIf
    ;thirst issues
    randy=Rnd(0,250)
    If pAnim(cyc)>1 And pAnim(cyc)<>5 And pAnim(cyc)<>33 And pAnim(cyc)<>37 Then randy=Rnd(0,25)
    If randy=0 Then pThirst(cyc)=pThirst(cyc)+1
    If pThirst(cyc)>100 Then pThirst(cyc)=100
    If pThirst(cyc)<0 Then pThirst(cyc)=0
    If randy=99 And pThirst(cyc)=>80 ;thirst pains
     ProduceSound(sCough,8000,0)
     If pHealth(cyc)>1 Then pHealth(cyc)=pHealth(cyc)-1
     pHP(cyc)=pHP(cyc)-1
    EndIf
    ;simulate hunger & thirst for CPU's
    randy=Rnd(0,3000)
    If randy=0 And pControl(cyc)=0 And pHunger(cyc)=>50 Then pHunger(cyc)=pHunger(cyc)-25
    If randy=1 And pControl(cyc)=0 And pThirst(cyc)=>50 Then pThirst(cyc)=pThirst(cyc)-25
    If pType(cyc)=3 Or pType(cyc)=>10 Then pHunger(cyc)=0 : pThirst(cyc)=0
    ;health issues
    randy=Rnd(0,250)
    If pControl(cyc)>0 And pAnim(cyc)>1 And pAnim(cyc)<>5 And pAnim(cyc)<>33 And pAnim(cyc)<>37
     randy=Rnd(0,50)
    EndIf
    If randy=0 And (pControl(cyc)>0 Or game=0)
     If pAnim(cyc)<>33 And pAnim(cyc)<>37 Then pHealth(cyc)=pHealth(cyc)-1 
    EndIf
    If randy=0 And pControl(cyc)=0 And game=>1 Then pHealth(cyc)=pHealth(cyc)+Rnd(0,1)
    If pHealth(cyc)>charHP(pType(cyc))*5 Then pHealth(cyc)=charHP(pType(cyc))*5
    If pHealth(cyc)<0 Then pHealth(cyc)=0
    If pDT(cyc)>999 And pHealth(cyc)>1 Then pHealth(cyc)=1
    randy=Rnd(0,100)
    If randy=0 Then pHP(cyc)=pHP(cyc)+1
    If pHP(cyc)>charHP(pType(cyc)) Then pHP(cyc)=charHP(pType(cyc))
    If pHealth(cyc)=>10 And pHP(cyc)>pHealth(cyc)/5 Then pHP(cyc)=pHealth(cyc)/5
    If pHealth(cyc)<10 And pHP(cyc)>2 Then pHP(cyc)=2
    pDT(cyc)=pDT(cyc)-1
    If pDT(cyc)<0 Then pDT(cyc)=0
    ;determine speed
    pOldSpeed#(cyc)=pSpeed#(cyc)
    pSpeed#(cyc)=charSpeed#(pType(cyc))
    If CarryState(cyc)=1 Or CarryState(cyc)=2 Then pSpeed#(cyc)=2.1
    If CarryState(cyc)=3 Then pSpeed#(cyc)=1.7
    If pY#(cyc)<optSeaLevel#-(charHeight(pType(cyc))/2) Then pSpeed#(cyc)=pSpeed#(cyc)-(pSpeed#(cyc)/3)
    If pHealth(cyc)=<20 Or pHunger(cyc)=>80 Or pThirst(cyc)=>80 Then pSpeed#(cyc)=pSpeed#(cyc)-(pSpeed#(cyc)/3) 
    ;footsteps
    stepped=0
    If pSpeed#(cyc)=>2.5 And pStepTim(cyc)>10 Then stepped=1
    If pSpeed#(cyc)=>2.1 And pSpeed#(cyc)<2.5 And pStepTim(cyc)>13 Then stepped=1
    If pSpeed#(cyc)=1.7 And pSpeed#(cyc)<2.1 And pStepTim(cyc)>16 Then stepped=1
    If stepped=1 And pType(cyc)<20 Then ProduceSound(sStep,8000,Rnd(0.15,0.4)) : pStepTim(cyc)=0
    ;water splashes
    If pAnim(cyc)>0 And pAnim(cyc)<>5 And pAnim(cyc)<>18 And pAnim(cyc)<>19 And pAnim(cyc)<>33 And pAnim(cyc)<>37
     If pY#(cyc)=<optSeaLevel# And pY#(cyc)>-(charHeight(pType(cyc))+5)
      WaterSpurt(pX#(cyc),optSeaLevel#,pZ#(cyc))
      pSplashTim(cyc)=pSplashTim(cyc)+1
      If pSplashTim(cyc)>15 Or pOldY#(cyc)>optSeaLevel#
       If pType(cyc)<>20 Then ProduceSound(sSplash,11025,Rnd(0.1,0.5))
       If pType(cyc)=20 Then ProduceSound(sSplash,11025,Rnd(0.0,0.2))
       pSplashTim(cyc)=0
      EndIf
     EndIf 
    EndIf
    ;transfer to display
    pA#(cyc)=CleanAngle(pA#(cyc))
    RotateEntity p(cyc),0,pA#(cyc),0
    RotateEntity pPivot(cyc),0,pA#(cyc),0
    PositionEntity p(cyc),pX#(cyc),pY#(cyc),pZ#(cyc)
    ;shadow
    ResetEntity pShadow(cyc)
    PositionEntity pShadow(cyc),pX#(cyc),pY#(cyc)+30,pZ#(cyc)
    EntityType pShadow(cyc),2,0
    EntityRadius pShadow(cyc),3,0.75
    MoveEntity pShadow(cyc),0,0,500
    ;manage accessories
    ManageAccessories(cyc)
    ;expressions
    FacialExpressions(cyc)
    ;scarring
    ManageScars(cyc)
    ;destruction!
    If pHealth(cyc)=<0 And AttackViable(cyc)=3 And pType(cyc)<>1 And pType(cyc)<>3
     ProduceSound(sDeath(charDeath(pType(cyc))),22050,1)
     ProduceSound(sVines,22050,1)
     CreateBurst(pX#(cyc),pY#(cyc)+5,pZ#(cyc),10,10,1)
     If charMeat(pType(cyc))>0 
      For count=1 To charMeat(pType(cyc))
       ReleaseItem(pX#(cyc),pY#(cyc),pZ#(cyc),4)
      Next
      If pType(cyc)<20 Then ReleaseItem(pX#(cyc),pY#(cyc),pZ#(cyc),18)
     EndIf
     pState(cyc)=0 
    EndIf
   EndIf
   ;remove
   If pState(cyc)=0 Then HideEntity p(cyc) : HideEntity pShadow(cyc)
  EndIf
 Next
 ;preserve old positions
 For cyc=1 To no_plays
  If pIsland(cyc)=gamIsland
   pOldX#(cyc)=pX#(cyc) : pOldY#(cyc)=pY#(cyc) : pOldZ#(cyc)=pZ#(cyc)
  EndIf
 Next
End Function

;--------------------------------------------------------------
;////////////////// RELATED FUNCTIONS /////////////////////////
;--------------------------------------------------------------

;APPLY MOVEMENT
Function ApplyMovement(cyc,speed#)
 ;turn
 If cLeft(cyc)=1 Then pA#(cyc)=pA#(cyc)+(speed#*3)
 If cRight(cyc)=1 Then pA#(cyc)=pA#(cyc)-(speed#*3)
 ;advance
 If cUp(cyc)=1 Then MoveEntity pPivot(cyc),0,0,speed#
 If pAnim(cyc)=2 Or pAnim(cyc)=3 Then backer#=speed# Else backer#=speed#/2
 If cDown(cyc)=1 Then MoveEntity pPivot(cyc),0,0,-backer#
End Function

;ENFORCE BLOCKS
Function EnforceBlocks(cyc)
 ;deflate nowhere counter
 If pNowhere(cyc)>0 Then pNowhere(cyc)=pNowhere(cyc)-1
 ;enemy bumping
 For v=1 To no_plays
  If pIsland(v)=gamIsland And pState(v)>0
   If charWidth(pType(v))<8 Then width=5 Else width=10
   height=charHeight(pType(v))
   If pOldX#(cyc)<pX#(v)+width And pOldX#(cyc)>pX#(v)-width And pOldZ#(cyc)<pZ#(v)+width And pOldZ#(cyc)>pZ#(v)-width And cyc<>v
    trapped=1
   Else
    If CollisionViable(cyc,v) And pX#(cyc)<pX#(v)+width And pX#(cyc)>pX#(v)-width And pZ#(cyc)<pZ#(v)+width And pZ#(cyc)>pZ#(v)-width And pY#(cyc)>pY#(v)-height And pY#(cyc)<pY#(v)+height
     If pOldX#(cyc)<pX#(v)+width And pOldX#(cyc)>pX#(v)-width Then pZ#(cyc)=pOldZ#(cyc)
     If pOldZ#(cyc)<pZ#(v)+width And pOldZ#(cyc)>pZ#(v)-width Then pX#(cyc)=pOldX#(cyc)
     PositionEntity pPivot(cyc),pX#(cyc),EntityY(pPivot(cyc)),pZ#(cyc)
     pNowhere(cyc)=pNowhere(cyc)+2
    EndIf
   EndIf
  EndIf
 Next
 ;movement obstructed?
 If pX#(cyc)>pOldX#(cyc)-0.5 And pX#(cyc)<pOldX#(cyc)+0.5 And pZ#(cyc)>pOldZ#(cyc)-0.5 And pZ#(cyc)<pOldZ#(cyc)+0.5
  If pAnim(cyc)=1 And cUp(cyc)=1 Then pNowhere(cyc)=pNowhere(cyc)+2
 EndIf
 ;mammals uncomfortable under water
 ;If pType(cyc)<20 And pY#(cyc)<-charHeight(pType(cyc)) Then pNowhere(cyc)=pNowhere(cyc)+2
 ;fish uncomfortable out of water
 ;If pType(cyc)=>20 And pY#(cyc)>-charHeight(pType(cyc)) Then pNowhere(cyc)=pNowhere(cyc)+2
 ;ground limit
 If pY#(cyc)<pGround#(cyc)-1
  pY#(cyc)=pGround#(cyc)
  PositionEntity pPivot(cyc),pX#(cyc),pY#(cyc)+18,pZ#(cyc)
 EndIf
 ;alert guard dog
 If gamIsland=5 And cyc<>42
  If pHP(cyc)>0 And pX#(cyc)<-180 And pZ#(cyc)>470 Then pAgenda(42)=1 : pTarget(42)=cyc
 EndIf
End Function

;FIND ATTACK IMPACTS
Function FindImpacts(cyc,range,hand,aim)
 ;find weapons
 carry=0
 If hand=1 And pCarryA(cyc)>0 Then carry=iType(pCarryA(cyc))
 If hand=2 And pCarryB(cyc)>0 Then carry=iType(pCarryB(cyc))
 ;initiate dummy
 PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity dummy,0,pA#(cyc),0
 If aim=3 Then span#=1.5 Else span#=3.0
 If hand=1 Then MoveEntity dummy,-span#,0,0
 If hand=2 Then MoveEntity dummy,span#,0,0
 ;ADVANCE THROUGH RANGE
 For depth=1 To range
  MoveEntity dummy,0,0,3
  checkX#=EntityX(dummy)
  checkZ#=EntityZ(dummy)
  ;find human matches
  For v=1 To no_plays
   width=8;charWidth(pType(v))
   height=charHeight(pType(v))
   If AttackViable(v)=aim Or (AttackViable(v)=1 And aim=3 And charHeight(pType(v))<10) Or (AttackViable(v)=3 And pType(cyc)=>10)
    If pSting(cyc)=1 And checkX#>pX#(v)-width And checkX#<pX#(v)+width And checkZ#>pZ#(v)-width And checkZ#<pZ#(v)+width And pY#(cyc)>pY#(v)-height And pY#(cyc)<pY#(v)+(height/3) And cyc<>v
     ProduceSound(sHit,10000,1)
     impactY#=pY#(cyc)+23
     If pAnim(cyc)=22 Or pAnim(cyc)=23 Then impactY#=pY#(cyc)+13
     If pAnim(cyc)=24 Or pAnim(cyc)=25 Or AttackViable(v)=3 Then impactY#=pY#(cyc)+5
     If impactY#>pY#(v)+(height-5) Then impactY#=pY#(v)+(height-5)
     If v=camFoc(1) Or width>8 Or AttackViable(v)=3
      ImpactSpurt(checkX#,impactY#,checkZ#) : BloodSpurt(checkX#,impactY#,checkZ#)
     Else
      ImpactSpurt(pX#(v),impactY#,pZ#(v)) : BloodSpurt(pX#(v),impactY#,pZ#(v))
     EndIf
     For count=1 To charPower(pType(cyc))
      ScarBody(v,checkX#,impactY#,checkZ#)
     Next
     If AttackViable(v)=<2 
      pAnim(v)=30 : pAnimTim(v)=0
      If pAnim(cyc)=22 Or pAnim(cyc)=23 Then pAnim(v)=31 : pAnimTim(v)=0
     EndIf
     If AttackViable(v)=3 Then pAnim(v)=34 : pAnimTim(v)=0
     pHealth(v)=pHealth(v)-charPower(pType(cyc))
     pHP(v)=pHP(v)-charPower(pType(cyc))
     If depth=<2 Then pStagger#(v)=1.0
     If depth=>3 And depth=<4 Then pStagger#(v)=0.5
     If depth=5 Then pStagger#(v)=0.25
     If (pType(cyc)=>10 And pType(cyc)=<12) Or (pType(v)=>10 And pType(v)=<12)
      pStagger#(v)=pStagger#(v)*2
     EndIf
     If carry>0 ;weapon bonus
      ProduceSound(iSound(carry),22050,0)
      pHealth(v)=pHealth(v)-iDamage(carry) 
      pHP(v)=pHP(v)-iDamage(carry)
      pStagger#(v)=pStagger#(v)+0.25
      BreakItem(cyc,hand,checkX#,impactY#,checkZ#,50)
      ScarBody(v,checkX#,impactY#,checkZ#)
      If (hand=1 And iFlaming(pCarryA(cyc))>0) Or (hand=2 And iFlaming(pCarryB(cyc))>0) ;fire bonus
       ProduceSound(sFireIgnite,22050,0)
       ProduceSound(sFireSinge,10000,1)
       FireSpurt(checkX#,impactY#,checkZ#) 
       ScarBody(v,checkX#,impactY#,checkZ#)
       pHealth(v)=pHealth(v)-1 : pHP(v)=pHP(v)-1
      EndIf
     EndIf
     If carry=0 Then ScarLimb(cyc,5+hand) : ScarLimb(cyc,7+hand) ;hurt hands
     If pHP(v)>0 Then pAgenda(cyc)=1 : pTarget(cyc)=v
     randy=Rnd(0,1)
     If randy=0 And charPower(pType(v))>0 Then pAgenda(v)=1 : pTarget(v)=cyc
     If randy>0 Or charPower(pType(v))=0 Then pNowhere(v)=99
     pSting(cyc)=0
     If pType(cyc)=1 And pHealth(v)=<0 And AttackViable(v)=3
      If pType(v)<10 Then AddCrime(3) : AddCrime(-1) ;human murder
      If pType(v)=>12 And pType(v)=<13 Then AddCrime(2) ;animal murder
      If pType(v)=10 And gamMission(slot)<>14 Then AddCrime(2) ;cat murder
      If pType(v)=11 And gamMission(slot)<>15 Then AddCrime(2) ;dog murder
     EndIf
    EndIf
   EndIf
  Next
  ;find resource matches
  For v=1 To no_resources
   width=resWidth(resType(v))
   height=resHeight(resType(v))
   If pSting(cyc)=1 And resIsland(v)=gamIsland And resState(v)>0 And checkX#>resX#(v)-width And checkX#<resX#(v)+width And checkZ#>resZ#(v)-width And checkZ#<resZ#(v)+width And pY#(cyc)>resY#(v)-25 And pY#(cyc)<resY#(v)+(height/2)
    ProduceSound(sHit,10000,1)
    ProduceSound(resSound(resType(v)),22050,0)
    impactY#=pY#(cyc)+23
    If pAnim(cyc)=22 Or pAnim(cyc)=23 Then impactY#=pY#(cyc)+13
    If pAnim(cyc)=24 Or pAnim(cyc)=25 Then impactY#=pY#(cyc)+5
    If impactY#>resY#(v)+(height+5) Then impactY#=resY#(v)+(height+5)
    ImpactSpurt(checkX#,impactY#,checkZ#)
    resAnim(v)=1 : resAnimTim(v)=0
    resHP(v)=resHP(v)-charPower(pType(cyc))
    If carry>0 ;weapon bonus
     ProduceSound(iSound(carry),22050,0)
     ImpactSpurt(checkX#,impactY#,checkZ#) 
     resHP(v)=resHP(v)-iDamage(carry) 
     BreakItem(cyc,hand,checkX#,impactY#,checkZ#,50)
     If (hand=1 And iFlaming(pCarryA(cyc))>0) Or (hand=2 And iFlaming(pCarryB(cyc))>0) ;fire bonus
      ProduceSound(sFireIgnite,22050,0)
      ProduceSound(sFireSinge,10000,1)
      FireSpurt(checkX#,impactY#,checkZ#) 
      resHP(v)=resHP(v)-1
     EndIf
    EndIf
    If carry=0 Then ScarLimb(cyc,5+hand) : ScarLimb(cyc,7+hand) ;hurt hands
    pSting(cyc)=0
   EndIf
  Next
  ;find material matches
  If aim=3 And pSting(cyc)=1 And pRecipe(cyc,0)=0
   For v=1 To no_items
    If iIsland(v)=gamIsland And iState(v)=1 And iType(v)=<3 And iBuilding(v)=0 And pRecipe(cyc,iType(v))=0
     If checkX#>iX#(v)-8 And checkX#<iX#(v)+8 And checkZ#>iZ#(v)-8 And checkZ#<iZ#(v)+8 And pY#(cyc)>iY#(v)-20 And pY#(cyc)<iY#(v)+20
      ProduceSound(sHit,10000,1)
      ProduceSound(iSound(iType(v)),22050,0)
      ImpactSpurt(checkX#,iY#(v)+1,checkZ#)
      CreateParticle(checkX#,iY#(v)+1,checkZ#,2)
      iX#(v)=iX#(v)+Rnd(-2,2) : iZ#(v)=iZ#(v)+Rnd(-2,2)
      iA#(v)=iA#(v)+Rnd(-30,30) : iGravity#(v)=0.5
      If carry>0 ;weapon bonus
       ProduceSound(iSound(carry),22050,0)
       BreakItem(cyc,hand,checkX#,impactY#,checkZ#,150)
       If iFlammable(carry)>0 And v=fireSource(gamIsland) And fireState(gamIsland)>0
        ProduceSound(sFireIgnite,22050,1)
        If hand=1 Then iFlaming(pCarryA(cyc))=iFlammable(carry)
        If hand=2 Then iFlaming(pCarryB(cyc))=iFlammable(carry)
       EndIf
      EndIf
      If carry=0 Then ScarLimb(cyc,7+hand) ;hurt hands
      pRecipe(cyc,iType(v))=pRecipe(cyc,iType(v))+1
      pRecipe(cyc,0)=1 : iBuilding(v)=cyc
     EndIf
    EndIf
   Next
   ;work on item
   If pRecipe(cyc,0)>0 Then ConstructRecipe(cyc,carry) : pSting(cyc)=0
  EndIf
 Next
End Function

;MANAGE SCARS
Function ManageScars(cyc)
 For limb=1 To 20
  If pLimb(cyc,limb)>0
   ;heal scars
   randy=Rnd(0,2500)
   If pScar(cyc,limb)=4 Or pType(cyc)=>10 Then randy=Rnd(0,1250)
   If EntityY(pLimb(cyc,limb),1)<optSeaLevel# And pScar(cyc,limb)>1 Then randy=Rnd(0,50)
   If randy=0 
    If EntityY(pLimb(cyc,limb),1)<optSeaLevel# And pScar(cyc,limb)>1
     limbX#=EntityX(pLimb(cyc,limb),1)
     limbY#=EntityY(pLimb(cyc,limb),1)
     limbZ#=EntityZ(pLimb(cyc,limb),1)
     CreateParticle(limbX#,limbY#,limbZ#,3)
    EndIf
    pScar(cyc,limb)=pScar(cyc,limb)-1
   EndIf
   If pScar(cyc,limb)<0 Then pScar(cyc,limb)=0
   ;prevent gore
   If optBlood=0 And pScar(cyc,limb)>1 Then pScar(cyc,limb)=1
   ;apply scars
   If pScar(cyc,limb)<>pOldScar(cyc,limb)
    If limb=1 Then EntityTexture pLimb(cyc,limb),tFaceScar(pScar(cyc,limb)),0,4
    If limb>1 Then EntityTexture pLimb(cyc,limb),tBodyScar(pScar(cyc,limb)),0,4
   EndIf
   ;store status
   pOldScar(cyc,limb)=pScar(cyc,limb)
  EndIf
 Next 
End Function

;COUNT SCARS
Function CountScars(cyc)
 value=0
 For limb=1 To 20
  If pLimb(cyc,limb)>0
   If pScar(cyc,limb)=>2 Or (optBlood=0 And pScar(cyc,limb)=>1)
    If limb=3 Then value=value+2 Else value=value+1
   EndIf
  EndIf
 Next 
 Return value
End Function

;SCAR SPECIFIC LIMB
Function ScarLimb(cyc,limb)
 randy=Rnd(0,6)
 If randy=0 And pLimb(cyc,limb)>0
  pScar(cyc,limb)=pScar(cyc,limb)+1
  If pScar(cyc,limb)>1 
   ProduceSound(sBleed,22050,0)
   limbX#=EntityX(pLimb(cyc,limb),1)
   limbY#=EntityY(pLimb(cyc,limb),1)
   limbZ#=EntityZ(pLimb(cyc,limb),1)
   CreateParticle(limbX#,limbY#,limbZ#,3)
  EndIf
 EndIf
 If pScar(cyc,limb)>4 Then pScar(cyc,limb)=4 
End Function

;SCAR WHOLE BODY
Function ScarBody(cyc,checkX#,checkY#,checkZ#)
 For count=1 To 20
  If pLimb(cyc,count)>0
   limbX#=EntityX(pLimb(cyc,count),1)
   limbY#=EntityY(pLimb(cyc,count),1)
   limbZ#=EntityZ(pLimb(cyc,count),1)
   If checkX#>limbX#-10 And checkX#<limbX#+10 And checkZ#>limbZ#-10 And checkZ#<limbZ#+10 And checkY#>limbY#-10 And checkY#<limbY#+10
    ScarLimb(cyc,count)
   EndIf
   If checkX#=0 And checkY#=0 And checkZ#=0 Then ScarLimb(cyc,count)
  EndIf
 Next
End Function

;CONSUME ITEM
Function ConsumeItem(cyc,item)
 ;benefits
 If iType(item)=1 Then pHunger(cyc)=pHunger(cyc)-15 : pThirst(cyc)=pThirst(cyc)+15
 If iType(item)=4 
  pHealth(cyc)=pHealth(cyc)+5 : pHunger(cyc)=pHunger(cyc)-30 : pThirst(cyc)=pThirst(cyc)+5
 EndIf
 If iType(item)=5 
  pHealth(cyc)=pHealth(cyc)+5 : pHunger(cyc)=pHunger(cyc)-20 : pThirst(cyc)=pThirst(cyc)-10
 EndIf
 ;poison!
 If iType(item)=2 Or iType(item)=3 Then pHealth(cyc)=pHealth(cyc)/2 : pThirst(cyc)=100
End Function

;SLEEP BENEFITS
Function SleepBenefits(cyc)
 ;find fire warmth
 If FireProximity(cyc,30) Then comfort=20 Else comfort=30 
 ;boost health
 randy=Rnd(0,comfort)
 If randy=0 And pY#(cyc)=>optSeaLevel#-5 Then pHealth(cyc)=pHealth(cyc)+1 : pHP(cyc)=pHP(cyc)+1
 ;drown!
 randy=Rnd(0,50)
 If randy=0 And pY#(cyc)<optSeaLevel#-5
  If pType(cyc)<10
   ProduceSound(sCough,8000,0)
   ProduceSound(sSplash,11025,Rnd(0.1,0.5))
  EndIf
  If pHealth(cyc)>1 Then pHealth(cyc)=pHealth(cyc)-1
  pThirst(cyc)=pThirst(cyc)-10
  limbX#=EntityX(pLimb(cyc,1),1)
  limbY#=EntityY(pLimb(cyc,1),1)
  limbZ#=EntityZ(pLimb(cyc,1),1)
  CreateParticle(limbX#,limbY#,limbZ#,6)
  CreateParticle(limbX#,limbY#,limbZ#,6)
 EndIf
End Function