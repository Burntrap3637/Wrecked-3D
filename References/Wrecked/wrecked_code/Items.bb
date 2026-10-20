;//////////////////////////////////////////////////////////////////////////////
;------------------------- WRECKED: ITEMS & RESOURCES -------------------------
;//////////////////////////////////////////////////////////////////////////////

;---------------------------------------------------------------------------------
;/////////////////////////////// RESOURCES ///////////////////////////////////////
;---------------------------------------------------------------------------------

;LOAD RESOURCES
Function LoadResources() 
 For cyc=1 To no_resources
  If resIsland(cyc)=gamIsland
   ;model
   res(cyc)=LoadAnimMesh("World/Resources/"+resName$(resType(cyc))+".3ds")
   ;location
   If resX#(cyc)=0 And resZ#(cyc)=0
    resX#(cyc)=Rnd(-400,400) : resZ#(cyc)=Rnd(0,400)
    resY#(cyc)=200 : resA#(cyc)=Rnd(0,360)
   EndIf
   If resY#(cyc)<-10 Then resY#(cyc)=100
   resY#(cyc)=resY#(cyc)+15
   PositionEntity res(cyc),resX#(cyc),resY#(cyc)+15,resZ#(cyc)
   RotateEntity res(cyc),0,resA#(cyc),0
   ;collision detection 
   EntityType res(cyc),4,1
   EntityRadius res(cyc),resWidth(resType(cyc))/2,resHeight(resType(cyc))/2
   If FindChild(res(cyc),"Block")>0 Then EntityAlpha FindChild(res(cyc),"Block"),0
   ;ignore leaves
   If FindChild(res(cyc),"Leaves")>0 Then EntityType FindChild(res(cyc),"Leaves"),0,0
   ;paint leaf types
   If resType(cyc)=2 Then EntityTexture FindChild(res(cyc),"Leaves"),tTree(5) ;palm tree
   If resType(cyc)=3 Then EntityTexture FindChild(res(cyc),"Leaves"),tTree(1) ;regular tree
   If resType(cyc)=4 Then EntityTexture FindChild(res(cyc),"Leaves"),tTree(2) ;fruit tree
   If resType(cyc)=5 Then EntityTexture FindChild(res(cyc),"Leaves"),tTree(3) ;regular bush
   If resType(cyc)=6 Then EntityTexture FindChild(res(cyc),"Leaves"),tTree(4) ;fruit bush
   ;values
   resAnim(cyc)=0 : resAnimTim(cyc)=0
  EndIf
 Next
End Function

;RESOURCE CYCLE
Function ResourceCycle()
 For cyc=1 To no_resources
  If resIsland(cyc)=gamIsland
   If resState(cyc)>0
    ;honour collision detection 
    resX#(cyc)=EntityX(res(cyc))
    resY#(cyc)=EntityY(res(cyc))
    resZ#(cyc)=EntityZ(res(cyc))
    ;allow resource planting
    If gamPlanting=1 And cyc=gamPlanter
     If KeyDown(200) Then resZ#(cyc)=resZ#(cyc)+3
     If KeyDown(208) Then resZ#(cyc)=resZ#(cyc)-3 
     If KeyDown(203) Then resX#(cyc)=resX#(cyc)-3
     If KeyDown(205) Then resX#(cyc)=resX#(cyc)+3 
     If KeyDown(14) Then resY#(cyc)=resY#(cyc)+10  
    EndIf
    ;force against ground
    If KeyDown(56) And KeyDown(201) Then resY#(cyc)=resY#(cyc)+5
    If gamCollisions=1 Then resY#(cyc)=resY#(cyc)-2.0
    ;shaking animation
    If resAnim(cyc)>0 
     If resAnimTim(cyc)=0 Then Animate res(cyc),3,Rnd(0.5,2.0),0,5
     If resAnimTim(cyc)>15 Then resAnim(cyc)=0
    EndIf
    resAnimTim(cyc)=resAnimTim(cyc)+1
    ;update display
    resA#(cyc)=CleanAngle(resA#(cyc))
    RotateEntity res(cyc),0,resA#(cyc),0
    PositionEntity res(cyc),resX#(cyc),resY#(cyc),resZ#(cyc) 
    ;regenerate
    randy=Rnd(0,200)
    If randy=0 Then resHP(cyc)=resHP(cyc)+1
    If resHP(cyc)>resStrength(resType(cyc)) Then resHP(cyc)=resStrength(resType(cyc))
    ;destruction!
    If resHP(cyc)=<0 
     ProduceSound(sVines,22050,1)
     CreateBurst(resX#(cyc),resY#(cyc),resZ#(cyc),resWidth(resType(cyc)),resHeight(resType(cyc)),0)
     ReleaseProduce(cyc)
     resState(cyc)=0 
    EndIf
   EndIf
   ;inactive
   If resState(cyc)=0 Then HideEntity res(cyc)
  EndIf
 Next
End Function

;---------------------------------------------------------------------------------
;////////////////////////////////// ITEMS ////////////////////////////////////////
;---------------------------------------------------------------------------------

;LOAD ITEMS
Function LoadItems() 
 For cyc=1 To no_items
  If iIsland(cyc)=gamIsland
   ;model
   If iType(cyc)=19
    i(cyc)=LoadAnimMesh("World/Items/"+iName$(iType(cyc))+".3ds")
   Else
    i(cyc)=LoadMesh("World/Items/"+iName$(iType(cyc))+".3ds")
   EndIf
   ;location
   PositionEntity i(cyc),iX#(cyc),iY#(cyc)+10,iZ#(cyc)
   RotateEntity i(cyc),0,iA#(cyc),0
   ScaleEntity i(cyc),0.4,0.4,0.4
   If iType(cyc)=17 Then EntityShininess i(cyc),1.0
   ;collision detection
   EntityType i(cyc),3,0
   If iType(cyc)<10 Or iType(cyc)-17 
    EntityRadius i(cyc),4,2
   Else 
    EntityRadius i(cyc),4,1
   EndIf
   ;values
   For v=1 To no_plays
    iSting(cyc,v)=0
   Next
  EndIf
 Next
End Function

;ITEM CYCLE
Function ItemCycle()
 For cyc=1 To no_items
  If iIsland(cyc)=gamIsland
   If iState(cyc)>0
    ;honour collision detection 
    iX#(cyc)=EntityX(i(cyc))
    iY#(cyc)=EntityY(i(cyc))
    iZ#(cyc)=EntityZ(i(cyc))
    ;check for GROUND bounce
    If iY#(cyc)=iOldY#(cyc) And iBouncer#(cyc)>0 And iGravity#(cyc)<-1
     If iBouncer#(cyc)>0.5 
      ProduceSound(iSound(iType(cyc)),22050,Rnd(0.1,0.3))
      If iType(cyc)=14 Then ProduceSound(sWood,22050,Rnd(0.1,0.3))
      DustSpurt(iX#(cyc),iY#(cyc),iZ#(cyc))
     EndIf
     iBouncer#(cyc)=iBouncer#(cyc)/2
     If iBouncer#(cyc)<0.2 Then iBouncer#(cyc)=0
     iGravity#(cyc)=iBouncer#(cyc) 
     iFlight#(cyc)=iFlight#(cyc)-(iFlight#(cyc)/3)
     If iFlight#(cyc)<0.2 Then iFlight#(cyc)=0
     If iFlaming(cyc)>1 Then iFlaming(cyc)=1
     For v=1 To no_plays
      iSting(cyc,v)=0
     Next
    EndIf
    ;check for WALL bounce
    If iY#(cyc)=iOldY#(cyc) And iX#(cyc)=iOldX#(cyc) And iZ#(cyc)=iOldZ#(cyc) And iFlight#(cyc)>0 And iBouncer#(cyc)>1.0
     ProduceSound(iSound(iType(cyc)),22050,Rnd(0.1,0.3))
     DustSpurt(iX#(cyc),iY#(cyc),iZ#(cyc))
     iGravity#(cyc)=1.0 : iFlight#(cyc)=-0.5
     iA#(cyc)=iA#(cyc)+Rnd(-20,20)
    EndIf
    ;find mid-air impacts with HUMANS
    If iY#(cyc)<>iOldY#(cyc)
     For v=1 To no_plays
      width=charWidth(pType(v))
      height=charHeight(pType(v))+3
      If AttackViable(v)=3 Then height=6
      If iSting(cyc,v)=1 And AttackViable(v)>0 And iX#(cyc)>pX#(v)-width And iX#(cyc)<pX#(v)+width And iZ#(cyc)>pZ#(v)-width And iZ#(cyc)<pZ#(v)+width And iY#(cyc)>pY#(v)-5 And iY#(cyc)<pY#(v)+height
       ProduceSound(iSound(iType(cyc)),22050,0)
       ProduceSound(sHit,11025,0.5)
       ImpactSpurt(iX#(cyc),iY#(cyc)-4,iZ#(cyc))
       BloodSpurt(iX#(cyc),iY#(cyc)-4,iZ#(cyc))
       ScarBody(v,iX#(cyc),iY#(cyc),iZ#(cyc))
       If AttackViable(v)=<2 And iY#(cyc)=>pY#(v)+18 Then pAnim(v)=30 : pAnimTim(v)=0 : pStagger#(v)=0.5
       If AttackViable(v)=<2 And iY#(cyc)=<pY#(v)+17 Then pAnim(v)=31 : pAnimTim(v)=0 : pStagger#(v)=0.5
       If AttackViable(v)=3 Then pAnim(v)=34 : pAnimTim(v)=0
       damager=iDamage(iType(cyc))*2
       If iType(cyc)=14 Then damager=iDamage(iType(cyc))*3
       pHealth(v)=pHealth(v)-iDamage(iType(cyc)) : pHP(v)=pHP(v)-damager
       If iFlaming(cyc)>0 ;fire bonus!
        ProduceSound(sFireIgnite,22050,0) 
        ProduceSound(sFireSinge,10000,1)
        FireSpurt(iX#(cyc),iY#(cyc)-4,iZ#(cyc))
        ScarBody(v,iX#(cyc),iY#(cyc),iZ#(cyc))
        pHealth(v)=pHealth(v)-1 : pHP(v)=pHP(v)-1
       EndIf
       iGravity#(cyc)=1.0 : iFlight#(cyc)=-0.5
       iA#(cyc)=iA#(cyc)+Rnd(-45,45)
       iSting(cyc,v)=0
       pNowhere(v)=99
      EndIf
     Next
    EndIf
    ;find mid-air impacts with RESOURCES
    If iY#(cyc)<>iOldY#(cyc)
     For v=1 To no_resources
      width=resWidth(resType(v))+1
      height=resHeight(resType(v))
      If resIsland(v)=gamIsland And iFlight#(cyc)>0 And iX#(cyc)>resX#(v)-width And iX#(cyc)<resX#(v)+width And iZ#(cyc)>resZ#(v)-width And iZ#(cyc)<resZ#(v)+width And iY#(cyc)>resY#(v)-height And iY#(cyc)<resY#(v)+(height*2) And resState(v)>0
       ProduceSound(sHit,11025,1)
       ProduceSound(iSound(iType(cyc)),22050,0)
       ProduceSound(resSound(resType(v)),22050,0)
       ImpactSpurt(iX#(cyc),iY#(cyc)-4,iZ#(cyc))
       resAnim(v)=1 : resAnimTim(v)=0
       resHP(v)=resHP(v)-iDamage(iType(cyc))
       If iType(cyc)=14 Then resHP(v)=resHP(v)-1 ;spear bonus
       iGravity#(cyc)=1.0 : iFlight#(cyc)=-0.5
       iA#(cyc)=iA#(cyc)+Rnd(-45,45)
   	  EndIf
  	 Next
    EndIf
    ;trap cycle
    TrapCycle(cyc)
    ;water splash
    If iY#(cyc)<optSeaLevel# And iOldY#(cyc)>optSeaLevel#
     WaterSpurt(iX#(cyc),optSeaLevel#,iZ#(cyc))
     WaterSpurt(iX#(cyc),optSeaLevel#,iZ#(cyc)) 
     ProduceSound(sSplash,11025,Rnd(0.1,0.5))
     If iFlaming(cyc)>1 Then iFlaming(cyc)=1
    EndIf
    ;enflamed weapon! 
    If iFlaming(cyc)>0
     For count=1 To 3
      CreateParticle(iX#(cyc),iY#(cyc)-1,iZ#(cyc),7)
     Next
     CreateParticle(iX#(cyc),iY#(cyc)-1,iZ#(cyc),5)
     randy=Rnd(0,3)
     If randy=0 Then CreateParticle(iX#(cyc),iY#(cyc)-1,iZ#(cyc),2)
     iFlaming(cyc)=iFlaming(cyc)-1
     If iFlaming(cyc)=<0 Then ProduceSound(sFireExpire,11000,1)
    EndIf
    ;store old positions 
    iOldX#(cyc)=iX#(cyc)
    iOldY#(cyc)=iY#(cyc)
    iOldZ#(cyc)=iZ#(cyc)
    ;flight & gravity
    If iGravity#(cyc)>-5.0 Then iGravity#(cyc)=iGravity#(cyc)-iWeight#(iType(cyc))
    If iY#(cyc)<optSeaLevel# And iGravity#(cyc)<-1.0 Then iGravity#(cyc)=-1.0
    If KeyDown(56) And KeyDown(201) Then iGravity#(cyc)=2.0
    If gamCollisions=0 Then iGravity#(cyc)=0
    MoveEntity i(cyc),0,iGravity#(cyc),iFlight#(cyc)
    iX#(cyc)=EntityX(i(cyc))
    iY#(cyc)=EntityY(i(cyc))
    iZ#(cyc)=EntityZ(i(cyc))
    ;update display
    iA#(cyc)=CleanAngle(iA#(cyc))
    RotateEntity i(cyc),0,iA#(cyc),0
    PositionEntity i(cyc),iX#(cyc),iY#(cyc),iZ#(cyc) 
   EndIf
   ;inactive
   If iState(cyc)=<0 Then HideEntity i(cyc)
  EndIf
 Next
End Function

;LOAD BOAT
Function LoadBoat() 
 cyc=gamIsland
 If cyc<>5
  ;model
  boat(cyc)=LoadAnimMesh("World/Items/Boat.3ds")
  EntityAlpha FindChild(boat(cyc),"Core"),0
  EntityType boat(cyc),10,1
  ;location
  If boatX#(cyc)=0 And boatZ#(cyc)=0 Then boatY#(cyc)=100
  boatY#(cyc)=boatY#(cyc)+10
  PositionEntity boat(cyc),boatX#(cyc),boatY#(cyc)+5,boatZ#(cyc)
  RotateEntity boat(cyc),0,boatA#(cyc),0
  ScaleEntity boat(cyc),0.4,0.4,0.4
  ;collision detection
  boatPivot(cyc)=CreatePivot()
  EntityType boatPivot(cyc),1,0
  EntityRadius boatPivot(cyc),30,12
  PositionEntity boatPivot(cyc),boatX#(cyc),boatY#(cyc)+5,boatZ#(cyc)
  ;values
  ;boatState(cyc)=9
  boatOldState(cyc)=-1
  boatBob#(cyc)=optSeaLevel#+5
 EndIf
End Function

;--------------------------------------------------------------
;////////////////// RELATED FUNCTIONS /////////////////////////
;--------------------------------------------------------------

;MANAGE PLAYER ACCESSORIES
Function ManageAccessories(cyc)
 If pType(cyc)<10
  ;resources
  HideEntity FindChild(p(cyc),"L_Vines")
  HideEntity FindChild(p(cyc),"R_Vines")
  If iType(pCarryA(cyc))=1 Then ShowEntity FindChild(p(cyc),"L_Vines")
  If iType(pCarryB(cyc))=1 Then ShowEntity FindChild(p(cyc),"R_Vines")
  HideEntity FindChild(p(cyc),"L_Wood")
  HideEntity FindChild(p(cyc),"R_Wood")
  If iType(pCarryA(cyc))=2 Then ShowEntity FindChild(p(cyc),"L_Wood")
  If iType(pCarryB(cyc))=2 Then ShowEntity FindChild(p(cyc),"R_Wood")
  HideEntity FindChild(p(cyc),"L_Stone")
  HideEntity FindChild(p(cyc),"R_Stone")
  If iType(pCarryA(cyc))=3 Then ShowEntity FindChild(p(cyc),"L_Stone")
  If iType(pCarryB(cyc))=3 Then ShowEntity FindChild(p(cyc),"R_Stone")
  HideEntity FindChild(p(cyc),"L_Meat")
  HideEntity FindChild(p(cyc),"R_Meat")
  If iType(pCarryA(cyc))=4 Then ShowEntity FindChild(p(cyc),"L_Meat")
  If iType(pCarryB(cyc))=4 Then ShowEntity FindChild(p(cyc),"R_Meat")
  HideEntity FindChild(p(cyc),"L_Fruit")
  HideEntity FindChild(p(cyc),"R_Fruit")
  If iType(pCarryA(cyc))=5 Then ShowEntity FindChild(p(cyc),"L_Fruit")
  If iType(pCarryB(cyc))=5 Then ShowEntity FindChild(p(cyc),"R_Fruit") 
  ;weapons
  HideEntity FindChild(p(cyc),"L_Whip")
  HideEntity FindChild(p(cyc),"R_Whip")
  If iType(pCarryA(cyc))=10 Then ShowEntity FindChild(p(cyc),"L_Whip")
  If iType(pCarryB(cyc))=10 Then ShowEntity FindChild(p(cyc),"R_Whip")
  HideEntity FindChild(p(cyc),"L_Stick")
  HideEntity FindChild(p(cyc),"R_Stick")
  If iType(pCarryA(cyc))=11 Then ShowEntity FindChild(p(cyc),"L_Stick")
  If iType(pCarryB(cyc))=11 Then ShowEntity FindChild(p(cyc),"R_Stick")
  HideEntity FindChild(p(cyc),"L_Rock")
  HideEntity FindChild(p(cyc),"R_Rock")
  If iType(pCarryA(cyc))=12 Then ShowEntity FindChild(p(cyc),"L_Rock")
  If iType(pCarryB(cyc))=12 Then ShowEntity FindChild(p(cyc),"R_Rock")
  HideEntity FindChild(p(cyc),"L_Hammer")
  HideEntity FindChild(p(cyc),"R_Hammer")
  If iType(pCarryA(cyc))=13 Then ShowEntity FindChild(p(cyc),"L_Hammer")
  If iType(pCarryB(cyc))=13 Then ShowEntity FindChild(p(cyc),"R_Hammer")
  HideEntity FindChild(p(cyc),"L_Spear")
  HideEntity FindChild(p(cyc),"R_Spear")
  If iType(pCarryA(cyc))=14 Then ShowEntity FindChild(p(cyc),"L_Spear")
  If iType(pCarryB(cyc))=14 Then ShowEntity FindChild(p(cyc),"R_Spear")
  HideEntity FindChild(p(cyc),"L_Axe")
  HideEntity FindChild(p(cyc),"R_Axe")
  If iType(pCarryA(cyc))=15 Then ShowEntity FindChild(p(cyc),"L_Axe")
  If iType(pCarryB(cyc))=15 Then ShowEntity FindChild(p(cyc),"R_Axe")
  HideEntity FindChild(p(cyc),"L_Gun")
  HideEntity FindChild(p(cyc),"R_Gun")
  If iType(pCarryA(cyc))=16 Then ShowEntity FindChild(p(cyc),"L_Gun")
  If iType(pCarryB(cyc))=16 Then ShowEntity FindChild(p(cyc),"R_Gun")
  HideEntity FindChild(p(cyc),"L_Artefact")
  HideEntity FindChild(p(cyc),"R_Artefact")
  If iType(pCarryA(cyc))=17 
   ShowEntity FindChild(p(cyc),"L_Artefact")
   EntityShininess FindChild(p(cyc),"L_Artefact"),1.0
  EndIf
  If iType(pCarryB(cyc))=17 
   ShowEntity FindChild(p(cyc),"R_Artefact")
   EntityShininess FindChild(p(cyc),"R_Artefact"),1.0
  EndIf
  HideEntity FindChild(p(cyc),"L_Bone")
  HideEntity FindChild(p(cyc),"R_Bone")
  If iType(pCarryA(cyc))=18 Then ShowEntity FindChild(p(cyc),"L_Bone")
  If iType(pCarryB(cyc))=18 Then ShowEntity FindChild(p(cyc),"R_Bone")
  HideEntity FindChild(p(cyc),"L_Oar")
  HideEntity FindChild(p(cyc),"R_Oar")
  If iType(pCarryA(cyc))=20 Then ShowEntity FindChild(p(cyc),"L_Oar")
  If iType(pCarryB(cyc))=20 Then ShowEntity FindChild(p(cyc),"R_Oar")
  ;make items follow
  If pCarryA(cyc)>0 
   PositionEntity i(pCarryA(cyc)),pX#(cyc),pY#(cyc)+25,pZ#(cyc)
   iX#(pCarryA(cyc))=pX#(cyc) : iZ#(pCarryA(cyc))=pZ#(cyc)
  EndIf
  If pCarryB(cyc)>0 
   PositionEntity i(pCarryB(cyc)),pX#(cyc),pY#(cyc)+25,pZ#(cyc)
   iX#(pCarryB(cyc))=pX#(cyc) : iZ#(pCarryB(cyc))=pZ#(cyc)
  EndIf
  ;enflame items
  If pCarryA(cyc)>0 And iFlaming(pCarryA(cyc))>0 Then BurnWeapon(cyc,pCarryA(cyc),1)
  If pCarryB(cyc)>0 And iFlaming(pCarryB(cyc))>0 Then BurnWeapon(cyc,pCarryB(cyc),2)
 EndIf
End Function

;ENFLAME WEAPON
Function BurnWeapon(cyc,item,hand)
 ;determine location
 If hand=1 Then namer$="L_"+iName$(iType(item))
 If hand=2 Then namer$="R_"+iName$(iType(item))
 limbX#=EntityX(FindChild(p(cyc),namer$),1)
 limbY#=EntityY(FindChild(p(cyc),namer$),1)
 limbZ#=EntityZ(FindChild(p(cyc),namer$),1)
 ;produce effects
 For count=1 To 3
  CreateParticle(limbX#,limbY#-1,limbZ#,7)
 Next
 CreateParticle(limbX#,limbY#-1,limbZ#,4) 
 CreateParticle(limbX#,limbY#-1,limbZ#,5) 
 randy=Rnd(0,3)
 If randy=0 Then CreateParticle(limbX#,limbY#-1,limbZ#,2)
 ;extinguish?
 iFlaming(item)=iFlaming(item)-1
 If iFlaming(item)<1 Then iFlaming(item)=1
 randy=Rnd(0,500)
 If (randy=0 And iFlaming(item)=<1) Or pY#(cyc)<optSeaLevel#-20
  ProduceSound(sFireExpire,11000,1)
  CreateParticle(limbX#,limbY#,limbZ#,2)
  iFlaming(item)=0
 EndIf
End Function

;CARRYING STATE (0=empty,1=left,2=right,3=full)
Function CarryState(cyc)
 value=0
 If pCarryA(cyc)>0 And iType(pCarryA(cyc))>0 And iType(pCarryA(cyc))<10 Then value=value+1
 If pCarryB(cyc)>0 And iType(pCarryB(cyc))>0 And iType(pCarryB(cyc))<10 Then value=value+2
 If pType(cyc)=>10 Then value=0
 Return value
End Function

;WEAPON STATE (0=empty,1=left,2=right)
Function WeaponState(cyc)
 value=0
 If pCarryA(cyc)>0 And iType(pCarryA(cyc))=>10 Then value=1
 If pCarryB(cyc)>0 And iType(pCarryB(cyc))=>10 Then value=2
 If pType(cyc)=>10 Then value=0
 Return value
End Function

;DROP ITEM
Function DropItem(cyc,hand)
 ;find subject
 v=0
 If hand=1 And pCarryA(cyc)>0 Then v=pCarryA(cyc) : pCarryA(cyc)=0
 If hand=2 And pCarryB(cyc)>0 Then v=pCarryB(cyc) : pCarryB(cyc)=0
 ;work from subject
 If v>0
  PositionEntity i(v),pX#(cyc),pY#(cyc),pZ#(cyc)
  RotateEntity i(v),0,pA#(cyc),0
  If hand=1 And iType(v)<10 Then MoveEntity i(v),-7,0,0 : iY#(v)=pY#(cyc)+18
  If hand=2 And iType(v)<10 Then MoveEntity i(v),7,0,0 : iY#(v)=pY#(cyc)+18
  If hand=1 And iType(v)=>10 Then MoveEntity i(v),-8,0,0 : iY#(v)=pY#(cyc)+19
  If hand=2 And iType(v)=>10 Then MoveEntity i(v),8,0,0 : iY#(v)=pY#(cyc)+19
  iX#(v)=EntityX(i(v)) : iZ#(v)=EntityZ(i(v))
  iA#(v)=Rnd(pA#(cyc)-20,pA#(cyc)+20)
  iGravity#(v)=1.0 : iFlight#(v)=0
  iBouncer#(v)=Rnd(1.5,2.5)
  iState(v)=1 : ShowEntity i(v)
  PositionEntity i(v),iX#(v),iY#(v),iZ#(v)
  For count=1 To no_plays
   iSting(v,count)=0
  Next
 EndIf
End Function

;THROW ITEM
Function ThrowItem(cyc,hand)
 ;find subject
 v=0
 If hand=1 And pCarryA(cyc)>0 Then v=pCarryA(cyc) : pCarryA(cyc)=0
 If hand=2 And pCarryB(cyc)>0 Then v=pCarryB(cyc) : pCarryB(cyc)=0
 ;work from subject
 If v>0
  PositionEntity i(v),pX#(cyc),pY#(cyc),pZ#(cyc)
  RotateEntity i(v),0,pA#(cyc),0
  If hand=1 Then MoveEntity i(v),-7,25,11
  If hand=2 Then MoveEntity i(v),6,25,11
  iX#(v)=EntityX(i(v)) : iZ#(v)=EntityZ(i(v))
  iY#(v)=pY#(cyc)+25 : iA#(v)=pA#(cyc)
  iGravity#(v)=1.2+pCharge#(cyc)
  iFlight#(v)=1.2+pCharge#(cyc)
  If iType(v)=>10 Then iFlight#(v)=iFlight#(v)+pCharge#(cyc)
  If iType(v)=14 Then iFlight#(v)=iFlight#(v)+pCharge#(cyc) 
  iBouncer#(v)=Rnd(1.5,2.5)
  iState(v)=1 : ShowEntity i(v)
  PositionEntity i(v),iX#(v),iY#(v),iZ#(v)
  For count=1 To no_plays
   iSting(v,count)=1
  Next
 EndIf
End Function

;RELEASE ITEM (from resource)
Function ReleaseItem(x#,y#,z#,item)
 ;find empty slot
 v=0
 For cyc=1 To no_items
  If iType(cyc)=item And iIsland(cyc)=gamIsland And iState(cyc)=0 Then v=cyc
 Next
 ;force any slot
 If v=0
  For cyc=1 To no_items
   If iType(cyc)=item And iIsland(cyc)=gamIsland And iState(cyc)=>0 Then v=cyc
  Next 
 EndIf
 ;place in world
 If v>0
  iX#(v)=x# : iY#(v)=y# : iZ#(v)=z#
  iA#(v)=Rnd(0,360) : iFlight#(v)=0.4
  If iType(v)=19 Then iFlight#(v)=0 : iTrapTim(v)=0
  iGravity#(v)=3.0 : iBouncer#(v)=Rnd(2.0,3.0)
  PositionEntity i(v),iX#(v),iY#(v),iZ#(v)
  iState(v)=1 : ShowEntity i(v)
 EndIf
End Function

;BREAK ITEM (in hand)
Function BreakItem(cyc,hand,x#,y#,z#,chance)
 ;logical risk
 randy=Rnd(0,chance)
 If game=1 And hand=1 And (iType(pCarryA(cyc))=16 Or iType(pCarryA(cyc))=17) Then randy=99
 If game=1 And hand=2 And (iType(pCarryB(cyc))=16 Or iType(pCarryB(cyc))=17) Then randy=99
 ;execute break
 If randy=0
  If hand=1 Then v=pCarryA(cyc) : pCarryA(cyc)=0 
  If hand=2 Then v=pCarryB(cyc) : pCarryB(cyc)=0 
  ProduceSound(sVines,22050,1)
  ProduceSound(iSound(iType(v)),20000,1)
  ImpactSpurt(x#,y#,z#)
  SmokeSpurt(x#,y#,z#)
  iState(v)=0
 EndIf
End Function

;RELEASE PRODUCE (from resource)
Function ReleaseProduce(cyc)
 ;vine content
 range=resVines(resType(cyc))
 If range>0
  For count=1 To range
   ReleaseItem(resX#(cyc),resY#(cyc),resZ#(cyc),1)
  Next
 EndIf
 ;wood content
 range=resWood(resType(cyc))
 If range>0
  For count=1 To range
   ReleaseItem(resX#(cyc),resY#(cyc),resZ#(cyc),2)
  Next
 EndIf
 ;stone content
 range=resStone(resType(cyc))
 If range>0
  For count=1 To range
   ReleaseItem(resX#(cyc),resY#(cyc),resZ#(cyc),3)
  Next
 EndIf
 ;fruit content
 range=resFruit(resType(cyc))
 If range>0
  For count=1 To range
   ReleaseItem(resX#(cyc),resY#(cyc),resZ#(cyc),5)
  Next
 EndIf
End Function

;RESET BUILDING STATUS
Function ResetBuilding(cyc)
 ;reset building recipe
 For count=0 To 5
  pRecipe(cyc,count)=0
 Next
 ;reset item identifiers 
 For v=1 To no_items
  iBuilding(v)=0
 Next
End Function

;CONSTRUCT FROM RECIPE
Function ConstructRecipe(cyc,tool)
 ;reset product status
 pOldProduct(cyc)=pProduct(cyc)
 pProduct(cyc)=0
 ;whip
 If pRecipe(cyc,1)>0 Then pProduct(cyc)=10
 ;stick
 If pRecipe(cyc,2)>0 Then pProduct(cyc)=11 
 ;rock
 If pRecipe(cyc,3)>0 Then pProduct(cyc)=12
 ;trap
 If pRecipe(cyc,1)>0 And tool=11 Then pProduct(cyc)=19 
 If pRecipe(cyc,2)>0 And tool=10 Then pProduct(cyc)=19
 If pRecipe(cyc,1)>0 And pRecipe(cyc,2)>0 And tool=0 Then pProduct(cyc)=19
 ;oar
 If tool=13 Or tool=15
  If pRecipe(cyc,2)>0 Then pProduct(cyc)=20
 EndIf 
 ;boat segment
 If tool=13 Or tool=15
  If pType(cyc)=1 And gamIsland<>5 And pRecipe(cyc,1)>0 And pRecipe(cyc,2)>0 
   If BoatProximity(cyc,35) Or boatState(gamIsland)=0 Then pProduct(cyc)=88
  EndIf
 EndIf
 ;spear
 If tool=13 Or tool=15
  If pRecipe(cyc,2)>0 And pRecipe(cyc,3)>0 Then pProduct(cyc)=14
 EndIf
 ;hammer
 If pRecipe(cyc,1)>0 And pRecipe(cyc,2)>0 And pRecipe(cyc,3)>0 Then pProduct(cyc)=13 
 ;axe
 If tool=13 Or tool=15
  If pRecipe(cyc,1)>0 And pRecipe(cyc,2)>0 And pRecipe(cyc,3)>0 Then pProduct(cyc)=15 
 EndIf  
 ;fire!
 If tool=11 Or tool=14 Or tool=20 
  If pRecipe(cyc,2)>0 Then pProduct(cyc)=99 
 EndIf
 ;RESULTS
 ;progress construction
 If tool=15 Then worker=2 Else worker=1
 If pProduct(cyc)=pOldProduct(cyc) Then pWork(cyc)=pWork(cyc)+worker Else pWork(cyc)=0
 ;produce item
 If pProduct(cyc)>0 And pProduct(cyc)<>99 And pWork(cyc)=>9
  For v=1 To no_items ;remove materials
   If iIsland(v)=gamIsland And iState(v)>0 And iBuilding(v)=cyc
    ProduceSound(sVines,22050,1)
    ImpactSpurt(iX#(v),iY#(v)+1,iZ#(v))
    SmokeSpurt(iX#(v),iY#(v)+1,iZ#(v))
    spawnX#=iX#(v) : spawnY#=iY#(v)+5 : spawnZ#=iZ#(v)
    iState(v)=0
   EndIf
  Next
  If pProduct(cyc)=<20 ;release item
   ReleaseItem(spawnX#,spawnY#,spawnZ#,pProduct(cyc))
   pAgenda(cyc)=3 : pTarget(cyc)=pProduct(cyc)
  EndIf
  If pProduct(cyc)=88 ;build boat
   ProduceSound(sWood,20000,1)
   v=gamIsland
   boatState(v)=boatState(v)+1
   If boatState(v)>9 Then boatState(v)=9
   If boatState(v)=<1 
    boatX#(v)=spawnX# : boatZ#(v)=spawnZ#
    boatY#(v)=spawnY#+5 : boatA#(v)=pA#(cyc)
    PositionEntity boatPivot(v),boatX#(v),boatY#(v),boatZ#(v)
   EndIf
  EndIf
  pProduct(cyc)=0 : pWork(cyc)=0
 EndIf
 ;produce fire
 If pProduct(cyc)=99 And pWork(cyc)=>9 
  For v=1 To no_items
   If iIsland(v)=gamIsland And iState(v)>0 And iBuilding(v)=cyc And iType(v)=2
    IgniteFire(iX#(v),iY#(v),iZ#(v))
    fireSource(gamIsland)=v
   EndIf
  Next
  pProduct(cyc)=0 : pWork(cyc)=0
 EndIf
End Function

;TRAP CYCLE
Function TrapCycle(cyc)
 ;find victims
 If iType(cyc)=19 And iState(cyc)=1 And iTrapTim(cyc)>20
  For v=1 To no_plays
   range=6
   If AttackViable(v)>0 And iState(cyc)=1 And iX#(cyc)>pX#(v)-range And iX#(cyc)<pX#(v)+range And iZ#(cyc)>pZ#(v)-range And iZ#(cyc)<pZ#(v)+range And iY#(cyc)>pY#(v)-5 And iY#(cyc)<pY#(v)+5
    Animate i(cyc),3,1,0,0
    ProduceSound(sVines,22050,1)
    ProduceSound(sWhip,22050,1)
    ProduceSound(sBone,22050,1)
    ProduceSound(sSpear,22050,1)
    ImpactSpurt(iX#(cyc),iY#(cyc)+5,iZ#(cyc))
    BloodSpurt(iX#(cyc),iY#(cyc)+5,iZ#(cyc))
    ScarBody(v,iX#(cyc),iY#(cyc),iZ#(cyc))
    If AttackViable(v)=<2 Then pAnim(v)=31 : pAnimTim(v)=0 : pStagger#(v)=0.0
    If AttackViable(v)=3 Then pAnim(v)=34 : pAnimTim(v)=0
    pHealth(v)=pHealth(v)-5 : pHP(v)=0
    iState(cyc)=3 : iTrapTim(cyc)=0
   EndIf
  Next
 EndIf 
 ;expire
 iTrapTim(cyc)=iTrapTim(cyc)+1
 If iState(cyc)=3 And iTrapTim(cyc)>50
  randy=Rnd(0,2)
  If randy=0
   ImpactSpurt(iX#(cyc),iY#(cyc),iZ#(cyc))
   SmokeSpurt(iX#(cyc),iY#(cyc),iZ#(cyc))
   iState(cyc)=0
  Else
   iState(cyc)=1 : iTrapTim(cyc)=0
  EndIf 
 EndIf
End Function

;BOAT CYCLE
Function BoatCycle()
 cyc=gamIsland
 If cyc<>5
  ;manage appearance
  If boatState(cyc)<>boatOldState(cyc)
   For count=1 To 11
    segment=FindChild(boat(cyc),"Boat"+Dig$(count,10))
    If boatState(cyc)=>count Or boatState(cyc)=>9 Then ShowEntity segment Else HideEntity segment
   Next
  EndIf
  If boatState(cyc)>0
   ;honour collision detection
   boatX#(cyc)=EntityX(boatPivot(cyc)) 
   boatY#(cyc)=EntityY(boatPivot(cyc)) 
   boatZ#(cyc)=EntityZ(boatPivot(cyc)) 
   boatA#(cyc)=EntityYaw(boatPivot(cyc))
   boatA#(cyc)=CleanAngle#(boatA#(cyc))
   If game=2 Then boatA#(cyc)=50
   ;gravity
   boatY#(cyc)=boatY#(cyc)-0.5
   If boatY#(cyc)>optSeaLevel#+10 Then boatY#(cyc)=boatY#(cyc)-1.0
   If boatY#(cyc)<optSeaLevel#+boatBob#(cyc) Then boatY#(cyc)=optSeaLevel#+boatBob#(cyc)
   ;bobbing
   If boatState(cyc)=>9
    If boatBob#(cyc)=<4.8 Then boatTarget#(cyc)=7.0
    If boatBob#(cyc)=>5.8 Then boatTarget#(cyc)=4.0
    If boatBob#(cyc)>boatTarget#(cyc) Then boatBob#(cyc)=boatBob#(cyc)-0.05
    If boatBob#(cyc)<boatTarget#(cyc) Then boatBob#(cyc)=boatBob#(cyc)+0.05
   EndIf
   ;update location
   PositionEntity boat(cyc),boatX#(cyc),boatY#(cyc),boatZ#(cyc)
   RotateEntity boat(cyc),0,boatA#(cyc),0
   PositionEntity boatPivot(cyc),boatX#(cyc),boatY#(cyc),boatZ#(cyc)
   RotateEntity boatPivot(cyc),0,boatA#(cyc),0
   ;riding commands
   If pAnim(1)=5 And pAnimTim(1)>10 
    speeder#=1.0
    If iType(pCarryA(1))=20 Then speeder#=speeder#+3.0
    If iType(pCarryB(1))=20 Then speeder#=speeder#+3.0
    If game=2 Then speeder#=2.0
    If cUp(1)=1 Or game=2 Then MoveEntity boatPivot(cyc),0,0,speeder#
    If cDown(1)=1 And game=<1 Then MoveEntity boatPivot(cyc),0,0,-(speeder#/2)
    If cLeft(1)=1 And game=<1 Then TurnEntity boatPivot(cyc),0,speeder#/5,0
    If cRight(1)=1 And game=<1 Then TurnEntity boatPivot(cyc),0,-(speeder#/5),0
    If (DirPressed(1) Or game=2) And boatY#(cyc)<optSeaLevel#+8
     randy=Rnd(0,20)
     If randy=0 Then ProduceSound(sSplash,11025,Rnd(0.1,0.7))
     For count=1 To 8
      limbX#=EntityX(FindChild(boat(cyc),"Boat0"+count),1)
      limbZ#=EntityZ(FindChild(boat(cyc),"Boat0"+count),1)
      CreateParticle(limbX#+Rnd(-8,8),optSeaLevel#,limbZ#+Rnd(-8,8),6)
     Next
    EndIf
   EndIf
  EndIf
 EndIf
End Function

;CHECK BOAT IS IN PROXIMITY
Function BoatProximity(cyc,range)
 value=0 : v=gamIsland
 If v<>5
  If pX#(cyc)>boatX#(v)-range And pX#(cyc)<boatX#(v)+range And pZ#(cyc)>boatZ#(v)-range And pZ#(cyc)<boatZ#(v)+range
   value=1
  EndIf
 EndIf
 Return value
End Function