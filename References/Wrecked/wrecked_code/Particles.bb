;---------------------------------------------------------------------------------
;/////////////////////// WRECKED: PARTICLE EFFECTS ///////////////////////////////
;---------------------------------------------------------------------------------

;LOAD PARTICLE EFFECTS
Function LoadParticles()
 For cyc=1 To no_particles
  part(cyc)=LoadSprite("World/Sprites/Particle.bmp")
  EntityFX part(cyc),8
  partState(cyc)=0
  HideEntity part(cyc)
 Next
End Function

;CREATE PARTICLE EFFECT
Function CreateParticle(x#,y#,z#,style)
 ;find empty spot
 cyc=0
 For count=1 To no_particles
  If partState(count)=0 Then cyc=count
 Next
 ;force spot!
 If cyc=0 And style<>1 Then cyc=Rnd(1,no_particles)
 ;activate new particle
 If cyc>0
  partX#(cyc)=x#
  partY#(cyc)=y#
  partZ#(cyc)=z#
  partA#(cyc)=Rnd(0,360)
  partGravity#(cyc)=Rnd(1.0,2.0)
  partFlight#(cyc)=0.3 
  partSize#(cyc)=Rnd(1.0,5.0)
  partAlpha#(cyc)=Rnd(0.5,0.9)
  ;unique traits
  partType(cyc)=style
  If partType(cyc)=1 Then EntityColor part(cyc),220,Rnd(0,100),0 ;fire
  If partType(cyc)=2 ;smoke
   randy=Rnd(0,100)
   EntityColor part(cyc),randy,randy,randy 
   partFlight#(cyc)=0.1
  EndIf
  If partType(cyc)=3 ;blood
   EntityColor part(cyc),Rnd(50,200),0,0
   partFlight#(cyc)=0.15 : partSize#(cyc)=Rnd(1.0,4.0)
   partGravity#(cyc)=Rnd(1.0,1.5)
   partAlpha#(cyc)=Rnd(0.8,1.0)
  EndIf
  If partType(cyc)=4 ;impact
   EntityColor part(cyc),Rnd(90,110),Rnd(70,90),Rnd(40,60);250,Rnd(100,200),0
   partFlight#(cyc)=0.15 : partSize#(cyc)=Rnd(1.0,4.0)
   partGravity#(cyc)=Rnd(1.0,1.5)
  EndIf
  If partType(cyc)=5 ;dust
   EntityColor part(cyc),100,80,50 
   partAlpha#(cyc)=Rnd(0.2,0.5)
   ;partSize#(cyc)=Rnd(1.0,4.0)
   partGravity#(cyc)=0.5
  EndIf
  If partType(cyc)=6 ;water
   EntityColor part(cyc),50,120,120 
   partAlpha#(cyc)=Rnd(0.1,0.4)
   partGravity#(cyc)=Rnd(0.5,1.0)
   partFlight#(cyc)=0.3 
  EndIf
  If partType(cyc)=7 ;small fire
   EntityColor part(cyc),220,Rnd(0,100),0
   partSize#(cyc)=Rnd(1.0,4.0)
   partGravity#(cyc)=Rnd(0.75,1.25)
  EndIf
  ;reset & show
  partTim(cyc)=0
  partState(cyc)=1
  ShowEntity part(cyc)
  PositionEntity part(cyc),partX#(cyc),partY#(cyc),partZ#(cyc)
  RotateEntity part(cyc),0,partA#(cyc),0
  ScaleSprite part(cyc),partSize#(cyc),partSize#(cyc)
  EntityAlpha part(cyc),partAlpha#(cyc)
 EndIf
End Function

;CREATE DUST SPURT
Function DustSpurt(x#,y#,z#)
 If optFX>0
  For count=1 To 8 
   CreateParticle(Rnd(x#-1,x#+1),Rnd(y#-1,y#+1),Rnd(z#-1,z#+1),5)
  Next
 EndIf
End Function

;CREATE IMPACT SPURT
Function ImpactSpurt(x#,y#,z#)
 If optFX>0
  For count=1 To 15 
   CreateParticle(Rnd(x#-1,x#+1),Rnd(y#-1,y#+1),Rnd(z#-1,z#+1),4)
  Next
 EndIf
End Function

;CREATE BLOOD SPURT
Function BloodSpurt(x#,y#,z#)
 If optFX>0 And optBlood>0
  For count=1 To 15 
   CreateParticle(Rnd(x#-1,x#+1),Rnd(y#-1,y#+1),Rnd(z#-1,z#+1),3)
  Next
 EndIf
End Function

;CREATE WATER SPURT
Function WaterSpurt(x#,y#,z#)
 If optFX>0
  For count=1 To 3 
   CreateParticle(Rnd(x#-5,x#+5),Rnd(y#-1,y#+1),Rnd(z#-5,z#+5),6)
  Next
 EndIf
End Function

;CREATE DRINK SPURT
Function DrinkSpurt(x#,y#,z#)
 For count=1 To 3 
  CreateParticle(Rnd(x#-2,x#+2),Rnd(y#-1,y#+1),Rnd(z#-2,z#+2),6)
 Next
End Function

;SMOKE SPURT
Function SmokeSpurt(x#,y#,z#)
 If optFX>0
  For count=1 To 10
   CreateParticle(Rnd(x#-5,x#+5),Rnd(y#-5,y#),Rnd(z#-5,z#+5),2)
  Next
 EndIf
End Function

;CREATE DESTRUCTION EXPLOSION
Function CreateBurst(x#,y#,z#,width,height,style)
 ;main burst
 ImpactSpurt(x#,y#,z#)
 If style=1 And optBlood>0 Then BloodSpurt(x#,y#,z#)
 For count=1 To 50 
  CreateParticle(Rnd(x#-width,x#+width),Rnd(y#-height,y#+height),Rnd(z#-width,z#+width),4)
 Next
 ;blood for animals
 If style=1 And optBlood>0
  For count=1 To 20 
   CreateParticle(Rnd(x#-width,x#+width),Rnd(y#-height,y#+height),Rnd(z#-width,z#+width),3)
  Next
 EndIf
 ;smoke 
 For count=1 To 5
  CreateParticle(Rnd(x#-width,x#+width),Rnd(y#-height,y#+height),Rnd(z#-width,z#+width),2)
 Next
End Function

;PARTICLE CYCLE
Function ParticleCycle()
 For cyc=1 To no_particles
  If partState(cyc)>0
   ;gravity
   If partGravity#(cyc)>-3.0 Then partGravity#(cyc)=partGravity#(cyc)-0.1
   If partType(cyc)=2 And partGravity#(cyc)<0.1 Then partGravity#(cyc)=0.1
   partY#(cyc)=partY#(cyc)+partGravity#(cyc)
   ;flight
   MoveEntity part(cyc),0,0,partFlight#(cyc)
   partX#(cyc)=EntityX(part(cyc))
   partZ#(cyc)=EntityZ(part(cyc))
   ;update properties
   PositionEntity part(cyc),partX#(cyc),partY#(cyc),partZ#(cyc)
   RotateEntity part(cyc),0,partA#(cyc),0
   ScaleSprite part(cyc),partSize#(cyc),partSize#(cyc)
   ;transparency
   If partType(cyc)=2 Then partAlpha#(cyc)=partAlpha#(cyc)-0.0075
   If partType(cyc)=6 Then partAlpha#(cyc)=partAlpha#(cyc)-0.015  
   If partType(cyc)=1 Or partType(cyc)=5 Then partAlpha#(cyc)=partAlpha#(cyc)-0.02
   If partType(cyc)=3 Or partType(cyc)=4 Or partType(cyc)=7 Then partAlpha#(cyc)=partAlpha#(cyc)-0.03 
   EntityAlpha part(cyc),partAlpha#(cyc)
   ;clock
   partTim(cyc)=partTim(cyc)+1
   If partAlpha#(cyc)=<0 Or partTim(cyc)>1000 Then partState(cyc)=0
  EndIf
  ;remove
  If partState(cyc)=0 Then HideEntity part(cyc)
 Next
End Function

;---------------------------------------------------------------------------------
;/////////////////////////////// FIRE ISSUES /////////////////////////////////////
;---------------------------------------------------------------------------------

;IGNITE FIRE
Function IgniteFire(x#,y#,z#)
 ;remove existing fire
 If fireState(gamIsland)=1 Then ExtinguishFire()
 ;ignition sound
 ProduceSound(sFireIgnite,22050,1)
 LoopSound sFireLoop
 chFire=PlaySound(sFireLoop)
 ;set new fire
 fireX#(gamIsland)=x#
 fireY#(gamIsland)=y#
 fireZ#(gamIsland)=z#
 fireState(gamIsland)=1
 fireTim(gamIsland)=0
End Function

;CREATE FIRE SPURT
Function FireSpurt(x#,y#,z#)
 ;blaze
 For count=1 To 7
  CreateParticle(Rnd(x#-5,x#+5),y#,Rnd(z#-5,z#+5),1)
 Next
 ;white base
 For count=1 To 3
  CreateParticle(x#,y#+1,z#,5)
 Next
 ;smoke
 CreateParticle(Rnd(x#-5,x#+5),y#,Rnd(z#-5,z#+5),2)
End Function

;FIRE CYCLE
Function FireCycle()
 cyc=gamIsland
 If fireState(cyc)=1
  ;follow source
  fireX#(cyc)=iX#(fireSource(cyc))
  fireY#(cyc)=iY#(fireSource(cyc))
  fireZ#(cyc)=iZ#(fireSource(cyc))
  ;blaze
  FireSpurt(fireX#(cyc),fireY#(cyc)+2,fireZ#(cyc))
  ;burn players!
  For v=1 To no_plays
   If fireTim(cyc)>5 And AttackViable(v)>0 And pAnim(v)<>34
    If pX#(v)>fireX#(cyc)-6 And pX#(v)<fireX#(cyc)+6 And pZ#(v)>fireZ#(cyc)-6 And pZ#(v)<fireZ#(cyc)+6 And pY#(v)>fireY#(cyc)-10 And pY#(v)<fireY#(cyc)+10
     ProduceSound(sFireSinge,10000,1)
     ImpactSpurt(pX#(v),fireY#(cyc)+5,pZ#(v))
     BloodSpurt(pX#(v),fireY#(cyc)+5,pZ#(v))
     ScarBody(v,fireX#(cyc),fireY#(cyc),fireZ#(cyc))
     If AttackViable(v)=<2 And pAnim(v)<>31 Then pAnim(v)=31 : pAnimTim(v)=0
     If AttackViable(v)=3 And pAnim(v)<>34 And pDT(v)>0 Then pAnim(v)=34 : pAnimTim(v)=0
     pHealth(v)=pHealth(v)-1 : pHP(v)=pHP(v)-1
     pStagger#(v)=1.0 : pNowhere(v)=99
    EndIf
   EndIf
  Next
  ;burn fuel
  fireTim(cyc)=fireTim(cyc)+1
  randy=Rnd(0,1000)
  If randy=0 And fireTim(cyc)=>iFlammable(iType(fireSource(cyc)))
   If fireSource(cyc)>0 
    ProduceSound(sFireExpire,11000,0)
    SmokeSpurt(fireX#(cyc),fireY#(cyc),fireZ#(cyc))
    iState(fireSource(cyc))=0
   EndIf
  EndIf
  ;replace fuel
  If fireSource(cyc)=0 Or iState(fireSource(cyc))=<0 Then SpreadFire(15)
 EndIf
 ;monitor foreign fires
 If gamMission(slot)<>16
  For cyc=1 To 5
   If cyc<>gamIsland And fireState(cyc)=1 
    fireTim(cyc)=fireTim(cyc)+1
    If fireTim(cyc)=>iFlammable(iType(fireSource(cyc)))
     iState(fireSource(cyc))=0
     fireState(cyc)=0
    EndIf
   EndIf
  Next
 EndIf
End Function

;SPREAD FIRE
Function SpreadFire(range)
 cyc=gamIsland
 ;find flammables in vicinity
 fireSource(cyc)=0
 For v=1 To no_items
  If iIsland(v)=gamIsland And iX#(v)>fireX#(cyc)-range And iX#(v)<fireX#(cyc)+range And iZ#(v)>fireZ#(cyc)-range And iZ#(v)<fireZ#(cyc)+range And iY#(v)>fireY#(cyc)-range And iY#(v)<fireY#(cyc)+range
   If iState(v)>0 And iFlammable(iType(v))>0 And fireSource(cyc)=0 And iY#(v)=>optSeaLevel#
    ProduceSound(sFireIgnite,22050,0)
    fireX#(cyc)=iX#(v) : fireY#(cyc)=iY#(v) : fireZ#(cyc)=iZ#(v)
    fireTim(cyc)=0 : fireSource(cyc)=v
   EndIf
  EndIf
 Next
 ;expire if no fuel
 If fireSource(cyc)=0 Then ExtinguishFire()
End Function

;EXTINGUISH FIRE
Function ExtinguishFire()
 cyc=gamIsland
 ;expire sound
 ProduceSound(sFireExpire,11000,1)
 If ChannelPlaying(chFire) Then StopChannel chFire 
 ;smoke particles
 SmokeSpurt(fireX#(cyc),fireY#(cyc),fireZ#(cyc))
 ;remove fire
 fireState(cyc)=0
End Function 

;PLAYER GETTING FIRE WARMTH?
Function FireProximity(cyc,range)
 value=0 : v=gamIsland
 If pX#(cyc)>fireX#(v)-range And pX#(cyc)<fireX#(v)+range And pZ#(cyc)>fireZ#(v)-range And pZ#(cyc)<fireZ#(v)+range And pY#(cyc)>fireY#(v)-range And pY#(cyc)<fireY#(v)+range
  If fireState(v)>0 Then value=1
 EndIf
 Return value
End Function