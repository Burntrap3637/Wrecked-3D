;//////////////////////////////////////////////////////////////////////////////
;---------------------------- WRECKED: ANIMATIONS -----------------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////// LOAD ANIMATION SEQUENCES ///////////////////////
Function LoadSequences(cyc)
 ;--------------------- HUMANS -------------------------------
 If pType(cyc)<10
  ;source files
  pSeq(cyc,200)=LoadAnimSeq(p(cyc),"Characters/Sequences/Standard.3ds")
  ;empty-handed
  pSeq(cyc,1)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;standing
  pSeq(cyc,2)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;running
  pSeq(cyc,3)=ExtractAnimSeq(p(cyc),440,490,pSeq(cyc,200)) ;left pick up
  pSeq(cyc,4)=ExtractAnimSeq(p(cyc),560,610,pSeq(cyc,200)) ;right pick up
  pSeq(cyc,5)=ExtractAnimSeq(p(cyc),680,695,pSeq(cyc,200)) ;left drop
  pSeq(cyc,6)=ExtractAnimSeq(p(cyc),730,745,pSeq(cyc,200)) ;right drop
  pSeq(cyc,7)=ExtractAnimSeq(p(cyc),780,820,pSeq(cyc,200)) ;left throw
  pSeq(cyc,8)=ExtractAnimSeq(p(cyc),880,920,pSeq(cyc,200)) ;right throw
  pSeq(cyc,9)=ExtractAnimSeq(p(cyc),1210,1280,pSeq(cyc,200)) ;left attack (high)
  pSeq(cyc,10)=ExtractAnimSeq(p(cyc),1130,1200,pSeq(cyc,200)) ;right attack (high)
  pSeq(cyc,11)=ExtractAnimSeq(p(cyc),1530,1600,pSeq(cyc,200)) ;left whip (high)
  pSeq(cyc,12)=ExtractAnimSeq(p(cyc),1450,1520,pSeq(cyc,200)) ;right whip (high)
  pSeq(cyc,13)=ExtractAnimSeq(p(cyc),2290,2360,pSeq(cyc,200)) ;left attack (low)
  pSeq(cyc,14)=ExtractAnimSeq(p(cyc),2130,2200,pSeq(cyc,200)) ;right attack (low)
  pSeq(cyc,15)=ExtractAnimSeq(p(cyc),2530,2600,pSeq(cyc,200)) ;left whip (low)
  pSeq(cyc,16)=ExtractAnimSeq(p(cyc),2450,2520,pSeq(cyc,200)) ;right whip (low)
  pSeq(cyc,17)=ExtractAnimSeq(p(cyc),3050,3120,pSeq(cyc,200)) ;left attack (ground)
  pSeq(cyc,18)=ExtractAnimSeq(p(cyc),2890,2960,pSeq(cyc,200)) ;right attack (ground)
  pSeq(cyc,19)=ExtractAnimSeq(p(cyc),3430,3510,pSeq(cyc,200)) ;left eat
  pSeq(cyc,20)=ExtractAnimSeq(p(cyc),3340,3420,pSeq(cyc,200)) ;right eat
  pSeq(cyc,21)=ExtractAnimSeq(p(cyc),3700,3730,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,22)=ExtractAnimSeq(p(cyc),3860,3890,pSeq(cyc,200)) ;hungry stance
  pSeq(cyc,23)=ExtractAnimSeq(p(cyc),3980,4010,pSeq(cyc,200)) ;thirsty stance
  pSeq(cyc,24)=ExtractAnimSeq(p(cyc),4730,4790,pSeq(cyc,200)) ;tired movement
  pSeq(cyc,25)=ExtractAnimSeq(p(cyc),4100,4160,pSeq(cyc,200)) ;hungry movement
  pSeq(cyc,26)=ExtractAnimSeq(p(cyc),4380,4440,pSeq(cyc,200)) ;thirsty movement
  ;left arm carrying
  pSeq(cyc,31)=ExtractAnimSeq(p(cyc),330,360,pSeq(cyc,200)) ;standing
  pSeq(cyc,32)=ExtractAnimSeq(p(cyc),370,430,pSeq(cyc,200)) ;running
  pSeq(cyc,33)=ExtractAnimSeq(p(cyc),440,490,pSeq(cyc,200)) ;left pick up
  pSeq(cyc,34)=ExtractAnimSeq(p(cyc),620,680,pSeq(cyc,200)) ;right pick up
  pSeq(cyc,35)=ExtractAnimSeq(p(cyc),680,695,pSeq(cyc,200)) ;left drop
  pSeq(cyc,36)=ExtractAnimSeq(p(cyc),755,770,pSeq(cyc,200)) ;right drop
  pSeq(cyc,37)=ExtractAnimSeq(p(cyc),780,820,pSeq(cyc,200)) ;left throw
  pSeq(cyc,38)=ExtractAnimSeq(p(cyc),930,970,pSeq(cyc,200)) ;right throw
  pSeq(cyc,39)=ExtractAnimSeq(p(cyc),1210,1280,pSeq(cyc,200)) ;left attack (high)
  pSeq(cyc,40)=ExtractAnimSeq(p(cyc),1290,1360,pSeq(cyc,200)) ;right attack (high)
  pSeq(cyc,41)=ExtractAnimSeq(p(cyc),1530,1600,pSeq(cyc,200)) ;left whip (high)
  pSeq(cyc,42)=ExtractAnimSeq(p(cyc),1610,1680,pSeq(cyc,200)) ;right whip (high)
  pSeq(cyc,43)=ExtractAnimSeq(p(cyc),2290,2360,pSeq(cyc,200)) ;left attack (low)
  pSeq(cyc,44)=ExtractAnimSeq(p(cyc),2210,2280,pSeq(cyc,200)) ;right attack (low)
  pSeq(cyc,45)=ExtractAnimSeq(p(cyc),2530,2600,pSeq(cyc,200)) ;left whip (low)
  pSeq(cyc,46)=ExtractAnimSeq(p(cyc),2610,2680,pSeq(cyc,200)) ;right whip (low)
  pSeq(cyc,47)=ExtractAnimSeq(p(cyc),3050,3120,pSeq(cyc,200)) ;left attack (ground)
  pSeq(cyc,48)=ExtractAnimSeq(p(cyc),2970,3040,pSeq(cyc,200)) ;right attack (ground)
  pSeq(cyc,49)=ExtractAnimSeq(p(cyc),3430,3510,pSeq(cyc,200)) ;left eat
  pSeq(cyc,50)=ExtractAnimSeq(p(cyc),3520,3600,pSeq(cyc,200)) ;right eat
  pSeq(cyc,51)=ExtractAnimSeq(p(cyc),3780,3810,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,52)=ExtractAnimSeq(p(cyc),3940,3970,pSeq(cyc,200)) ;hungry stance
  pSeq(cyc,53)=ExtractAnimSeq(p(cyc),4020,4050,pSeq(cyc,200)) ;thirsty stance
  pSeq(cyc,54)=ExtractAnimSeq(p(cyc),4800,4860,pSeq(cyc,200)) ;tired movement
  pSeq(cyc,55)=ExtractAnimSeq(p(cyc),4170,4230,pSeq(cyc,200)) ;hungry movement
  pSeq(cyc,56)=ExtractAnimSeq(p(cyc),4520,4580,pSeq(cyc,200)) ;thirsty movement
  ;right arm carrying
  pSeq(cyc,61)=ExtractAnimSeq(p(cyc),220,250,pSeq(cyc,200)) ;standing
  pSeq(cyc,62)=ExtractAnimSeq(p(cyc),260,320,pSeq(cyc,200)) ;running
  pSeq(cyc,63)=ExtractAnimSeq(p(cyc),500,550,pSeq(cyc,200)) ;left pick up
  pSeq(cyc,64)=ExtractAnimSeq(p(cyc),440,490,pSeq(cyc,200)) ;right pick up
  pSeq(cyc,65)=ExtractAnimSeq(p(cyc),705,720,pSeq(cyc,200)) ;left drop
  pSeq(cyc,66)=ExtractAnimSeq(p(cyc),730,745,pSeq(cyc,200)) ;right drop
  pSeq(cyc,67)=ExtractAnimSeq(p(cyc),830,870,pSeq(cyc,200)) ;left throw 
  pSeq(cyc,68)=ExtractAnimSeq(p(cyc),880,920,pSeq(cyc,200)) ;right throw
  pSeq(cyc,69)=ExtractAnimSeq(p(cyc),1370,1440,pSeq(cyc,200)) ;left attack (high)
  pSeq(cyc,70)=ExtractAnimSeq(p(cyc),1130,1200,pSeq(cyc,200)) ;right attack (high)
  pSeq(cyc,71)=ExtractAnimSeq(p(cyc),1690,1760,pSeq(cyc,200)) ;left whip (high)
  pSeq(cyc,72)=ExtractAnimSeq(p(cyc),1450,1520,pSeq(cyc,200)) ;right whip (high)
  pSeq(cyc,73)=ExtractAnimSeq(p(cyc),2370,2440,pSeq(cyc,200)) ;left attack (low)
  pSeq(cyc,74)=ExtractAnimSeq(p(cyc),2130,2200,pSeq(cyc,200)) ;right attack (low)
  pSeq(cyc,75)=ExtractAnimSeq(p(cyc),2690,2760,pSeq(cyc,200)) ;left whip (low)
  pSeq(cyc,76)=ExtractAnimSeq(p(cyc),2450,2520,pSeq(cyc,200)) ;right whip (low)
  pSeq(cyc,77)=ExtractAnimSeq(p(cyc),3130,3200,pSeq(cyc,200)) ;left attack (ground)
  pSeq(cyc,78)=ExtractAnimSeq(p(cyc),2890,2960,pSeq(cyc,200)) ;right attack (ground)
  pSeq(cyc,79)=ExtractAnimSeq(p(cyc),3610,3690,pSeq(cyc,200)) ;left eat
  pSeq(cyc,80)=ExtractAnimSeq(p(cyc),3340,3420,pSeq(cyc,200)) ;right eat
  pSeq(cyc,81)=ExtractAnimSeq(p(cyc),3740,3770,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,82)=ExtractAnimSeq(p(cyc),3900,3930,pSeq(cyc,200)) ;hungry stance
  pSeq(cyc,83)=ExtractAnimSeq(p(cyc),4060,4090,pSeq(cyc,200)) ;thirsty stance
  pSeq(cyc,84)=ExtractAnimSeq(p(cyc),4870,4930,pSeq(cyc,200)) ;tired movement
  pSeq(cyc,85)=ExtractAnimSeq(p(cyc),4240,4300,pSeq(cyc,200)) ;hungry movement
  pSeq(cyc,86)=ExtractAnimSeq(p(cyc),4450,4510,pSeq(cyc,200)) ;thirsty movement
  ;arms full
  pSeq(cyc,91)=ExtractAnimSeq(p(cyc),110,140,pSeq(cyc,200)) ;standing
  pSeq(cyc,92)=ExtractAnimSeq(p(cyc),150,210,pSeq(cyc,200)) ;running
  pSeq(cyc,93)=ExtractAnimSeq(p(cyc),500,550,pSeq(cyc,200)) ;left pick up
  pSeq(cyc,94)=ExtractAnimSeq(p(cyc),620,680,pSeq(cyc,200)) ;right pick up
  pSeq(cyc,95)=ExtractAnimSeq(p(cyc),705,720,pSeq(cyc,200)) ;left drop
  pSeq(cyc,96)=ExtractAnimSeq(p(cyc),755,770,pSeq(cyc,200)) ;right drop
  pSeq(cyc,97)=ExtractAnimSeq(p(cyc),830,870,pSeq(cyc,200)) ;left throw
  pSeq(cyc,98)=ExtractAnimSeq(p(cyc),930,970,pSeq(cyc,200)) ;right throw
  pSeq(cyc,99)=ExtractAnimSeq(p(cyc),1370,1440,pSeq(cyc,200)) ;left attack (high)
  pSeq(cyc,100)=ExtractAnimSeq(p(cyc),1290,1360,pSeq(cyc,200)) ;right attack (high)
  pSeq(cyc,101)=ExtractAnimSeq(p(cyc),1690,1760,pSeq(cyc,200)) ;left whip (high)
  pSeq(cyc,102)=ExtractAnimSeq(p(cyc),1610,1680,pSeq(cyc,200)) ;right whip (high)
  pSeq(cyc,103)=ExtractAnimSeq(p(cyc),2370,2440,pSeq(cyc,200)) ;left attack (low) 
  pSeq(cyc,104)=ExtractAnimSeq(p(cyc),2210,2280,pSeq(cyc,200)) ;right attack (low)
  pSeq(cyc,105)=ExtractAnimSeq(p(cyc),2690,2760,pSeq(cyc,200)) ;left whip (low)
  pSeq(cyc,106)=ExtractAnimSeq(p(cyc),2610,2680,pSeq(cyc,200)) ;right whip (low)
  pSeq(cyc,107)=ExtractAnimSeq(p(cyc),3130,3200,pSeq(cyc,200)) ;left attack (ground)
  pSeq(cyc,108)=ExtractAnimSeq(p(cyc),2970,3040,pSeq(cyc,200)) ;right attack (ground)
  pSeq(cyc,109)=ExtractAnimSeq(p(cyc),3610,3690,pSeq(cyc,200)) ;left eat
  pSeq(cyc,110)=ExtractAnimSeq(p(cyc),3520,3600,pSeq(cyc,200)) ;right eat
  pSeq(cyc,111)=ExtractAnimSeq(p(cyc),3820,3850,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,112)=ExtractAnimSeq(p(cyc),3820,3850,pSeq(cyc,200)) ;hungry stance
  pSeq(cyc,113)=ExtractAnimSeq(p(cyc),3820,3850,pSeq(cyc,200)) ;thirsty stance
  pSeq(cyc,114)=ExtractAnimSeq(p(cyc),4310,4370,pSeq(cyc,200)) ;tired movement
  pSeq(cyc,115)=ExtractAnimSeq(p(cyc),4310,4370,pSeq(cyc,200)) ;hungry movement
  pSeq(cyc,116)=ExtractAnimSeq(p(cyc),4310,4370,pSeq(cyc,200)) ;thirsty movement
  ;universal
  pSeq(cyc,121)=ExtractAnimSeq(p(cyc),980,1030,pSeq(cyc,200)) ;jump
  pSeq(cyc,122)=ExtractAnimSeq(p(cyc),1040,1070,pSeq(cyc,200)) ;fall
  pSeq(cyc,123)=ExtractAnimSeq(p(cyc),2070,2120,pSeq(cyc,200)) ;left flying attack
  pSeq(cyc,124)=ExtractAnimSeq(p(cyc),2010,2060,pSeq(cyc,200)) ;right flying attack
  pSeq(cyc,130)=ExtractAnimSeq(p(cyc),1925,2000,pSeq(cyc,200)) ;hurt (upper)
  pSeq(cyc,131)=ExtractAnimSeq(p(cyc),3260,3330,pSeq(cyc,200)) ;hurt (lower)
  pSeq(cyc,132)=ExtractAnimSeq(p(cyc),1770,1875,pSeq(cyc,200)) ;collapse
  pSeq(cyc,133)=ExtractAnimSeq(p(cyc),1885,1915,pSeq(cyc,200)) ;lying
  pSeq(cyc,134)=ExtractAnimSeq(p(cyc),3210,3250,pSeq(cyc,200)) ;hurt (lying)
  pSeq(cyc,135)=ExtractAnimSeq(p(cyc),2770,2875,pSeq(cyc,200)) ;get up
  pSeq(cyc,136)=ExtractAnimSeq(p(cyc),4600,4680,pSeq(cyc,200)) ;go to sleep
  pSeq(cyc,137)=ExtractAnimSeq(p(cyc),4690,4720,pSeq(cyc,200)) ;sleep loop
  pSeq(cyc,138)=ExtractAnimSeq(p(cyc),4940,5050,pSeq(cyc,200)) ;dying threat!
  pSeq(cyc,139)=ExtractAnimSeq(p(cyc),5060,5090,pSeq(cyc,200)) ;boat driving
  pSeq(cyc,140)=ExtractAnimSeq(p(cyc),5100,5130,pSeq(cyc,200)) ;meditation
 EndIf
 ;-------------------------- LEOPARD --------------------------------
 If pType(cyc)=10
  ;source files
  pSeq(cyc,200)=LoadAnimSeq(p(cyc),"Characters/Sequences/Cat.3ds")
  ;standard sequences
  pSeq(cyc,1)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;standing
  pSeq(cyc,2)=ExtractAnimSeq(p(cyc),40,90,pSeq(cyc,200)) ;running
  pSeq(cyc,14)=ExtractAnimSeq(p(cyc),390,460,pSeq(cyc,200)) ;attack
  pSeq(cyc,130)=ExtractAnimSeq(p(cyc),100,175,pSeq(cyc,200)) ;hurt (upper)
  pSeq(cyc,131)=ExtractAnimSeq(p(cyc),100,175,pSeq(cyc,200)) ;hurt (lower)
  pSeq(cyc,132)=ExtractAnimSeq(p(cyc),185,260,pSeq(cyc,200)) ;collapse
  pSeq(cyc,133)=ExtractAnimSeq(p(cyc),270,300,pSeq(cyc,200)) ;lying
  pSeq(cyc,134)=ExtractAnimSeq(p(cyc),310,380,pSeq(cyc,200)) ;hurt (lying)
  ;last resorts
  pSeq(cyc,21)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,24)=ExtractAnimSeq(p(cyc),40,90,pSeq(cyc,200)) ;tired movement 
  pSeq(cyc,121)=ExtractAnimSeq(p(cyc),40,90,pSeq(cyc,200)) ;jump
  pSeq(cyc,122)=ExtractAnimSeq(p(cyc),40,90,pSeq(cyc,200)) ;fall
 EndIf
 ;-------------------------- WOLF --------------------------------
 If pType(cyc)=11
  ;source files
  pSeq(cyc,200)=LoadAnimSeq(p(cyc),"Characters/Sequences/Dog.3ds")
  ;standard sequences
  pSeq(cyc,1)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;standing
  pSeq(cyc,2)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;running
  pSeq(cyc,14)=ExtractAnimSeq(p(cyc),400,470,pSeq(cyc,200)) ;attack
  pSeq(cyc,130)=ExtractAnimSeq(p(cyc),120,185,pSeq(cyc,200)) ;hurt (upper)
  pSeq(cyc,131)=ExtractAnimSeq(p(cyc),120,185,pSeq(cyc,200)) ;hurt (lower)
  pSeq(cyc,132)=ExtractAnimSeq(p(cyc),195,270,pSeq(cyc,200)) ;collapse
  pSeq(cyc,133)=ExtractAnimSeq(p(cyc),280,310,pSeq(cyc,200)) ;lying
  pSeq(cyc,134)=ExtractAnimSeq(p(cyc),320,390,pSeq(cyc,200)) ;hurt (lying)
  ;last resorts
  pSeq(cyc,21)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,24)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;tired movement 
  pSeq(cyc,122)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;fall
 EndIf
 ;-------------------------- DEER --------------------------------
 If pType(cyc)=12
  ;source files
  pSeq(cyc,200)=LoadAnimSeq(p(cyc),"Characters/Sequences/Deer.3ds")
  ;standard sequences
  pSeq(cyc,1)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;standing
  pSeq(cyc,2)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;running
  pSeq(cyc,130)=ExtractAnimSeq(p(cyc),110,180,pSeq(cyc,200)) ;hurt (upper)
  pSeq(cyc,131)=ExtractAnimSeq(p(cyc),110,180,pSeq(cyc,200)) ;hurt (lower)
  pSeq(cyc,132)=ExtractAnimSeq(p(cyc),190,260,pSeq(cyc,200)) ;collapse
  pSeq(cyc,133)=ExtractAnimSeq(p(cyc),270,300,pSeq(cyc,200)) ;lying
  pSeq(cyc,134)=ExtractAnimSeq(p(cyc),310,380,pSeq(cyc,200)) ;hurt (lying)
  ;last resorts
  pSeq(cyc,21)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,24)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;tired movement 
  pSeq(cyc,122)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;fall
 EndIf
 ;-------------------------- RABBIT --------------------------------
 If pType(cyc)=13
  ;source files
  pSeq(cyc,200)=LoadAnimSeq(p(cyc),"Characters/Sequences/Rabbit.3ds")
  ;standard sequences
  pSeq(cyc,1)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;resting
  pSeq(cyc,2)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;movement
  pSeq(cyc,130)=ExtractAnimSeq(p(cyc),110,180,pSeq(cyc,200)) ;hurt (upper)
  pSeq(cyc,131)=ExtractAnimSeq(p(cyc),110,180,pSeq(cyc,200)) ;hurt (lower)
  pSeq(cyc,132)=ExtractAnimSeq(p(cyc),190,260,pSeq(cyc,200)) ;collapse
  pSeq(cyc,133)=ExtractAnimSeq(p(cyc),270,300,pSeq(cyc,200)) ;lying
  pSeq(cyc,134)=ExtractAnimSeq(p(cyc),310,380,pSeq(cyc,200)) ;hurt (lying)
  ;last resorts
  pSeq(cyc,21)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,24)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;tired movement 
  pSeq(cyc,122)=ExtractAnimSeq(p(cyc),40,100,pSeq(cyc,200)) ;fall
 EndIf
 ;-------------------------- FISH --------------------------------
 If pType(cyc)=20
  ;source files
  pSeq(cyc,200)=LoadAnimSeq(p(cyc),"Characters/Sequences/Fish.3ds")
  ;standard sequences
  pSeq(cyc,1)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;resting
  pSeq(cyc,2)=ExtractAnimSeq(p(cyc),40,70,pSeq(cyc,200)) ;swimming
  pSeq(cyc,130)=ExtractAnimSeq(p(cyc),80,150,pSeq(cyc,200)) ;hurt (upper)
  pSeq(cyc,131)=ExtractAnimSeq(p(cyc),80,150,pSeq(cyc,200)) ;hurt (lower)
  pSeq(cyc,132)=ExtractAnimSeq(p(cyc),160,230,pSeq(cyc,200)) ;collapse
  pSeq(cyc,133)=ExtractAnimSeq(p(cyc),240,270,pSeq(cyc,200)) ;lying
  pSeq(cyc,134)=ExtractAnimSeq(p(cyc),280,350,pSeq(cyc,200)) ;hurt (lying)
  ;last resorts
  pSeq(cyc,21)=ExtractAnimSeq(p(cyc),0,30,pSeq(cyc,200)) ;tired stance
  pSeq(cyc,24)=ExtractAnimSeq(p(cyc),40,70,pSeq(cyc,200)) ;tired movement 
  pSeq(cyc,122)=ExtractAnimSeq(p(cyc),40,70,pSeq(cyc,200)) ;fall
 EndIf
End Function

;//////////////////////// EXECUTE ANIMATIONS ////////////////////////////////
Function PlayerAnims(cyc)
 ;--------------------------- MOVEMENT STATES -----------------------------
 ;rest
 If pAnim(cyc)=0
  If pAnimTim(cyc)=0
   anim=1+(CarryState(cyc)*30) : speeder#=Rnd(0.2,0.4)
   If pHealth(cyc)=<20 Then anim=21+(CarryState(cyc)*30) ;tired 
   If pHunger(cyc)=>80 Then anim=22+(CarryState(cyc)*30) ;hungry
   If pThirst(cyc)=>80 And WeaponState(cyc)=0 Then anim=23+(CarryState(cyc)*30) ;thirsty
   If pThirst(cyc)=>80 And WeaponState(cyc)>0 Then anim=21+(CarryState(cyc)*30) ;last resort thirsty
   If pType(cyc)=3 Then anim=140 : speeder#=Rnd(0.1,0.2)
   Animate p(cyc),1,speeder#,pSeq(cyc,anim),10
  EndIf
 EndIf
 ;movement
 If pAnim(cyc)=1
  If pAnimTim(cyc)=0 Or pSpeed#(cyc)<>pOldSpeed#(cyc)
   anim=2+(CarryState(cyc)*30)
   If pHealth(cyc)=<20 Then anim=24+(CarryState(cyc)*30) ;tired 
   If pHunger(cyc)=>80 Then anim=25+(CarryState(cyc)*30) ;hungry
   If pThirst(cyc)=>80 And WeaponState(cyc)=0 Then anim=26+(CarryState(cyc)*30) ;thirsty
   If pThirst(cyc)=>80 And WeaponState(cyc)>0 Then anim=24+(CarryState(cyc)*30) ;last resort thirsty
   Animate p(cyc),1,pSpeed#(cyc)+charAnim#(pType(cyc)),pSeq(cyc,anim),5
  EndIf
  ApplyMovement(cyc,pSpeed#(cyc))
  If pAnimTim(cyc)>5 And DirPressed(cyc)=0 Then ChangeAnim(cyc,0)
  pStepTim(cyc)=pStepTim(cyc)+1
 EndIf
 ;jump
 If pAnim(cyc)=2
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,2,pSeq(cyc,121),5
  jumper#=pSpeed#(cyc)/2;pSpeed#(cyc)-(pSpeed#(cyc)/3)
  If pAnimTim(cyc)=>10 And pAnimTim(cyc)<15 Then ApplyMovement(cyc,jumper#)
  If pAnimTim(cyc)=>15 Then ApplyMovement(cyc,jumper#)
  If pAnimTim(cyc)=15 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>15 Then pGravity#(cyc)=jumper#
  If pAnimTim(cyc)=>25 Then ChangeAnim(cyc,3)
  pSpeaking(cyc)=1
 EndIf
 ;fall
 If pAnim(cyc)=3
  If pAnimTim(cyc)=0
   Animate p(cyc),1,1,pSeq(cyc,122),10
   If pOldAnim(cyc)=<2 And pGravity#(cyc)<-1 Then pGravity#(cyc)=-1
  EndIf
  jumper#=pSpeed#(cyc)/2;pSpeed#(cyc)-(pSpeed#(cyc)/3)
  MoveEntity pPivot(cyc),0,0,jumper#/2
  ApplyMovement(cyc,jumper#)
  If pY#(cyc)<pGround#(cyc)+2 Then ChangeAnim(cyc,4)
  pSpeaking(cyc)=1
 EndIf
 ;land
 If pAnim(cyc)=4
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,1,pSeq(cyc,1),5
  If pAnimTim(cyc)=0 And gotim>5 ;impact issues
   ProduceSound(sLand,22050,0)
   If pGravity#(cyc)=<-3 
    ProduceSound(sPain(charPain(pType(cyc))),22050,0)
    ScarBody(cyc,pX#(cyc),pY#(cyc),pZ#(cyc))
    pHealth(cyc)=pHealth(cyc)-1 : pHP(cyc)=pHP(cyc)-1
    If pGravity#(cyc)=<-4 Then pHealth(cyc)=pHealth(cyc)-5 : pHP(cyc)=pHP(cyc)-5
    If pGravity#(cyc)=<-5 Then pHealth(cyc)=pHealth(cyc)-10 : pHP(cyc)=0
   EndIf
   PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
   RotateEntity dummy,0,pA#(cyc),0
   MoveEntity dummy,5,0,0
   DustSpurt(EntityX(dummy),pY#(cyc)-2,EntityZ(dummy))
   MoveEntity dummy,-10,0,0
   DustSpurt(EntityX(dummy),pY#(cyc)-2,EntityZ(dummy))
  EndIf
  randy=Rnd(0,15)
  If randy=0 And pAnimTim(cyc)=1 Then DropItem(cyc,1)
  If randy=1 And pAnimTim(cyc)=1 Then DropItem(cyc,2)
  If pAnimTim(cyc)=2 Then pGravity#(cyc)=1.0
  If pAnimTim(cyc)=>3 Then ChangeAnim(cyc,0)
 EndIf
 ;boat rider
 If pAnim(cyc)=5
  v=gamIsland
  If pAnimTim(cyc)=0 Then Animate p(cyc),1,Rnd(0.2,0.4),pSeq(cyc,139),10
  EntityType FindChild(boat(v),"Boat11"),0
  If pX#(cyc)<boatX#(v) Then pX#(cyc)=pX#(cyc)+1
  If pX#(cyc)>boatX#(v) Then pX#(cyc)=pX#(cyc)-1
  If pZ#(cyc)<boatZ#(v) Then pZ#(cyc)=pZ#(cyc)+1
  If pZ#(cyc)>boatZ#(v) Then pZ#(cyc)=pZ#(cyc)-1
  If pA#(cyc)<boatA#(v)-10 Or pA#(cyc)>boatA#(v)+10 
   pA#(cyc)=pA#(cyc)+ReachAngle#(pA#(cyc),boatA#(v),10)
  EndIf
  If pAnimTim(cyc)<10 Then pY#(cyc)=boatY#(v)-((pAnimTim(cyc)/2)+1)
  If pAnimTim(cyc)=10 Then ProduceSound(sFall,8000,0.5) 
  If pAnimTim(cyc)=>10 
   pX#(cyc)=boatX#(v) : pZ#(cyc)=boatZ#(v)
   pY#(cyc)=boatY#(v)-6 : pA#(cyc)=boatA#(v)
   If ActionPressed(cyc) Then ChangeAnim(cyc,6)
  EndIf
  PositionEntity pPivot(cyc),pX#(cyc),pY#(cyc)+18,pZ#(cyc)
  pGravity#(cyc)=0
 EndIf
 ;dismount boat
 If pAnim(cyc)=6
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,1,pSeq(cyc,1),10
  pY#(cyc)=boatY#(gamIsland)-(8-pAnimTim(cyc))
  If pAnimTim(cyc)=8 Then EntityType FindChild(boat(gamIsland),"Boat11"),10,1
  If pAnimTim(cyc)=>8 Then ChangeAnim(cyc,0)
  PositionEntity pPivot(cyc),pX#(cyc),pY#(cyc)+18,pZ#(cyc)
  pGravity#(cyc)=0
  pStepTim(cyc)=pStepTim(cyc)+2
 EndIf
 ;--------------------------- INTERACTIONS ---------------------------------------
 ;left pick up
 If pAnim(cyc)=10
  If pAnimTim(cyc)=0
   anim=3+(CarryState(cyc)*30) 
   Animate p(cyc),3,2,pSeq(cyc,anim),5
  EndIf
  If pAnimTim(cyc)=6 Then ProduceSound(sSwing,22050,0.2)
  If pAnimTim(cyc)=12
   For v=1 To no_items
    If ItemProximity(cyc,v,20) And iState(v)=1 And iType(v)<>19 And pCarryA(cyc)=0
     If ItemInRange(cyc,v,8)
      ProduceSound(sShuffle,22050,1)
      ProduceSound(iSound(iType(v)),22050,0.2)
      If iType(v)=14 Then ProduceSound(sWood,22050,0.2)
      DustSpurt(iX#(v),iY#(v),iZ#(v))
      pCarryA(cyc)=v : iState(v)=-1
      pNowhere(cyc)=99
     EndIf
    EndIf
   Next
  EndIf
  If pControl(cyc)=0 And pCarryA(cyc)>0 And pHunger(cyc)>50 
   If iType(pCarryA(cyc))=4 Or iType(pCarryA(cyc))=5 Then cPickA(cyc)=1 ;CPU eat
  EndIf
  If pControl(cyc)=0 And pCarryA(cyc)=0 And pY#(cyc)<optSeaLevel#-5 Then cPickA(cyc)=1 ;CPU drink
  If pAnimTim(cyc)=>20 And cPickA(cyc)=1 
   If pCarryA(cyc)>0 And iType(pCarryA(cyc))<10 Then ChangeAnim(cyc,16) ;proceed to eat 
   If pCarryA(cyc)=0 And pY#(cyc)<optSeaLevel#-1 Then ChangeAnim(cyc,18) ;proceed to drink
  EndIf
  If pAnimTim(cyc)=>25 Then ChangeAnim(cyc,0)
 EndIf
 ;right pick up
 If pAnim(cyc)=11
  If pAnimTim(cyc)=0
   anim=4+(CarryState(cyc)*30)
   Animate p(cyc),3,2,pSeq(cyc,anim),5
  EndIf
  If pAnimTim(cyc)=6 Then ProduceSound(sSwing,22050,0.2)
  If pAnimTim(cyc)=12
   For v=1 To no_items
    If ItemProximity(cyc,v,20) And iState(v)=1 And iType(v)<>19 And pCarryB(cyc)=0
     If ItemInRange(cyc,v,8)
      ProduceSound(sShuffle,22050,1)
      ProduceSound(iSound(iType(v)),22050,0.2)
      If iType(v)=14 Then ProduceSound(sWood,22050,0.2)
      DustSpurt(iX#(v),iY#(v),iZ#(v))
      pCarryB(cyc)=v : iState(v)=-1
      pNowhere(cyc)=99
     EndIf
    EndIf
   Next
  EndIf
  If pControl(cyc)=0 And pCarryB(cyc)>0 And pHunger(cyc)>50 
   If iType(pCarryB(cyc))=4 Or iType(pCarryB(cyc))=5 Then cPickB(cyc)=1 ;CPU eat
  EndIf
  If pControl(cyc)=0 And pCarryB(cyc)=0 And pY#(cyc)<optSeaLevel#-5 Then cPickB(cyc)=1 ;CPU drink
  If pAnimTim(cyc)=>20 And cPickB(cyc)=1
   If pCarryB(cyc)>0 And iType(pCarryB(cyc))<10 Then ChangeAnim(cyc,17) ;proceed to eat
   If pCarryB(cyc)=0 And pY#(cyc)<optSeaLevel#-1 Then ChangeAnim(cyc,19) ;proceed to drink
  EndIf
  If pAnimTim(cyc)=>25 Then ChangeAnim(cyc,0)
 EndIf
 ;left drop
 If pAnim(cyc)=12
  If pAnimTim(cyc)=0
   anim=5+(CarryState(cyc)*30) 
   Animate p(cyc),3,0.8,pSeq(cyc,anim),10
  EndIf 
  If pAnimTim(cyc)=0 Then ProduceSound(sSwing,22050,0.2) : DropItem(cyc,1)
  If pAnimTim(cyc)=>15 Or (pAnimTim(cyc)=>5 And DirPressed(cyc)) Then ChangeAnim(cyc,0)
 EndIf
 ;right drop
 If pAnim(cyc)=13
  If pAnimTim(cyc)=0 
   anim=6+(CarryState(cyc)*30)
   Animate p(cyc),3,0.8,pSeq(cyc,anim),10
  EndIf
  If pAnimTim(cyc)=0 Then ProduceSound(sSwing,22050,0.2) : DropItem(cyc,2)
  If pAnimTim(cyc)=>15 Or (pAnimTim(cyc)=>5 And DirPressed(cyc)) Then ChangeAnim(cyc,0)
 EndIf 
 ;left throw
 If pAnim(cyc)=14
  If pAnimTim(cyc)=0 
   anim=7+(CarryState(cyc)*30)
   Animate p(cyc),3,1.5,pSeq(cyc,anim),10
   pCharge#(cyc)=0
  EndIf
  If pAnimTim(cyc)=8 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=<14 And cAttackA(cyc)=1 Then pCharge#(cyc)=pCharge#(cyc)+0.075
  If pAnimTim(cyc)=14 Then ThrowItem(cyc,1)
  If pAnimTim(cyc)=>25 Then ChangeAnim(cyc,0)
 EndIf
 ;right throw
 If pAnim(cyc)=15
  If pAnimTim(cyc)=0 
   anim=8+(CarryState(cyc)*30)
   Animate p(cyc),3,1.5,pSeq(cyc,anim),10
   pCharge#(cyc)=0
  EndIf
  If pAnimTim(cyc)=8 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=<14 And cAttackB(cyc)=1 Then pCharge#(cyc)=pCharge#(cyc)+0.075
  If pAnimTim(cyc)=14 Then ThrowItem(cyc,2)
  If pAnimTim(cyc)=>25 Then ChangeAnim(cyc,0)
 EndIf
 ;left eat
 If pAnim(cyc)=16
  If pAnimTim(cyc)=0
   anim=19+(CarryState(cyc)*30) 
   Animate p(cyc),3,2,pSeq(cyc,anim),8
  EndIf 
  If pAnimTim(cyc)=>5 And pAnimTim(cyc)=<35 Then pSpeaking(cyc)=1
  If pAnimTim(cyc)=10 Then ProduceSound(sEat,22050,1) : ConsumeItem(cyc,pCarryA(cyc))
  If pAnimTim(cyc)=20 Then ProduceSound(iSound(iType(pCarryA(cyc))),22050,0.5)
  If pAnimTim(cyc)=20 And pThirst(cyc)>90 Then ProduceSound(sCough,8000,1)
  handX#=EntityX(pLimb(cyc,8),1) : handY#=EntityY(pLimb(cyc,8),1) : handZ#=EntityZ(pLimb(cyc,8),1)
  If pAnimTim(cyc)=20 Or pAnimTim(cyc)=30 Then DustSpurt(handX#,handY#,handZ#)
  If pAnimTim(cyc)=>30 And pCarryA(cyc)>0 Then iState(pCarryA(cyc))=0 : pCarryA(cyc)=0
  If pAnimTim(cyc)=>40 Then ChangeAnim(cyc,0)
 EndIf
 ;right eat
 If pAnim(cyc)=17
  If pAnimTim(cyc)=0
   anim=20+(CarryState(cyc)*30) 
   Animate p(cyc),3,2,pSeq(cyc,anim),8
  EndIf 
  If pAnimTim(cyc)=>5 And pAnimTim(cyc)=<35 Then pSpeaking(cyc)=1
  If pAnimTim(cyc)=10 Then ProduceSound(sEat,22050,1) : ConsumeItem(cyc,pCarryB(cyc))
  If pAnimTim(cyc)=20 Then ProduceSound(iSound(iType(pCarryB(cyc))),22050,0.5)
  If pAnimTim(cyc)=20 And pThirst(cyc)>90 Then ProduceSound(sCough,8000,1) 
  handX#=EntityX(pLimb(cyc,9),1) : handY#=EntityY(pLimb(cyc,9),1) : handZ#=EntityZ(pLimb(cyc,9),1)
  If pAnimTim(cyc)=20 Or pAnimTim(cyc)=30 Then DustSpurt(handX#,handY#,handZ#)
  If pAnimTim(cyc)=>30 And pCarryB(cyc)>0 Then iState(pCarryB(cyc))=0 : pCarryB(cyc)=0
  If pAnimTim(cyc)=>40 Then ChangeAnim(cyc,0)
 EndIf 
 ;left drink
 If pAnim(cyc)=18
  If pAnimTim(cyc)=0
   anim=19+(CarryState(cyc)*30) 
   Animate p(cyc),3,2,pSeq(cyc,anim),8
  EndIf 
  If pAnimTim(cyc)=>5 And pAnimTim(cyc)=<35 Then pSpeaking(cyc)=1
  If pAnimTim(cyc)=10 Then ProduceSound(sDrink,22050,1) : pThirst(cyc)=pThirst(cyc)-15
  handX#=EntityX(pLimb(cyc,8),1) : handY#=EntityY(pLimb(cyc,8),1) : handZ#=EntityZ(pLimb(cyc,8),1)
  If pAnimTim(cyc)=<30 Then DrinkSpurt(handX#,handY#,handZ#)
  If pAnimTim(cyc)=>40 Then ChangeAnim(cyc,0)
 EndIf
 ;right drink
 If pAnim(cyc)=19
  If pAnimTim(cyc)=0
   anim=20+(CarryState(cyc)*30) 
   Animate p(cyc),3,2,pSeq(cyc,anim),8
  EndIf 
  If pAnimTim(cyc)=>5 And pAnimTim(cyc)=<35 Then pSpeaking(cyc)=1
  If pAnimTim(cyc)=10 Then ProduceSound(sDrink,22050,1) : pThirst(cyc)=pThirst(cyc)-15
  handX#=EntityX(pLimb(cyc,9),1) : handY#=EntityY(pLimb(cyc,9),1) : handZ#=EntityZ(pLimb(cyc,9),1)
  If pAnimTim(cyc)=<30 Then DrinkSpurt(handX#,handY#,handZ#)
  If pAnimTim(cyc)=>40 Then ChangeAnim(cyc,0)
 EndIf 
 ;----------------------------- ATTACKING ----------------------------------------
 ;left HIGH attack
 If pAnim(cyc)=20
  If pAnimTim(cyc)=0
   If pCarryA(cyc)=0 Then anim=9+(CarryState(cyc)*30)
   If pCarryA(cyc)>0 Then anim=iAnim(iType(pCarryA(cyc)))+(CarryState(cyc)*30)
   Animate p(cyc),3,2.5,pSeq(cyc,anim),5
   pSting(cyc)=1
  EndIf
  If pAnimTim(cyc)=3 Then ProduceSound(sAttack(charAttack(pType(cyc))),22050,0)
  If pAnimTim(cyc)=8 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>3 And pAnimTim(cyc)=<15 Then ApplyMovement(cyc,0.25)
  If pCarryA(cyc)>0 Then range=iRange(iType(pCarryA(cyc))) Else range=charRange(pType(cyc))
  If pAnimTim(cyc)=14 Then FindImpacts(cyc,range,1,1)
  If pAnimTim(cyc)=>25 Or (pAnimTim(cyc)=>20 And cAttackA(cyc)=0 And cAttackB(cyc)=1)
   If pCarryA(cyc)>0 And iType(pCarryA(cyc))<10 And pSting(cyc)=0 Then DropItem(cyc,1)
   ChangeAnim(cyc,0)
  EndIf
  pSpeaking(cyc)=1
 EndIf
 ;right HIGH attack
 If pAnim(cyc)=21
  If pAnimTim(cyc)=0
   If pCarryB(cyc)=0 Then anim=10+(CarryState(cyc)*30)
   If pCarryB(cyc)>0 Then anim=(iAnim(iType(pCarryB(cyc)))+1)+(CarryState(cyc)*30)
   Animate p(cyc),3,2.5,pSeq(cyc,anim),5
   pSting(cyc)=1
  EndIf
  If pAnimTim(cyc)=3 Then ProduceSound(sAttack(charAttack(pType(cyc))),22050,0)
  If pAnimTim(cyc)=8 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>3 And pAnimTim(cyc)=<15 Then ApplyMovement(cyc,0.25)
  If pCarryB(cyc)>0 Then range=iRange(iType(pCarryB(cyc))) Else range=charRange(pType(cyc))
  If pAnimTim(cyc)=14 Then FindImpacts(cyc,range,2,1)
  If pAnimTim(cyc)=>25 Or (pAnimTim(cyc)=>20 And cAttackA(cyc)=1 And cAttackB(cyc)=0)
   If pCarryB(cyc)>0 And iType(pCarryB(cyc))<10 And pSting(cyc)=0 Then DropItem(cyc,2)
   ChangeAnim(cyc,0)
  EndIf
  pSpeaking(cyc)=1
 EndIf
 ;left LOW attack
 If pAnim(cyc)=22
  If pAnimTim(cyc)=0
   If pCarryA(cyc)=0 Then anim=13+(CarryState(cyc)*30)
   If pCarryA(cyc)>0 Then anim=(iAnim(iType(pCarryA(cyc)))+4)+(CarryState(cyc)*30)
   Animate p(cyc),3,2.5,pSeq(cyc,anim),5
   pSting(cyc)=1
  EndIf
  If pAnimTim(cyc)=3 Then ProduceSound(sAttack(charAttack(pType(cyc))),22050,0)
  If pAnimTim(cyc)=8 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>3 And pAnimTim(cyc)=<15 Then ApplyMovement(cyc,0.25)
  If pCarryA(cyc)>0 Then range=iRange(iType(pCarryA(cyc))) Else range=charRange(pType(cyc))
  If pAnimTim(cyc)=14 Then FindImpacts(cyc,range,1,1)
  If pAnimTim(cyc)=>25 Or (pAnimTim(cyc)=>20 And cAttackA(cyc)=0 And cAttackB(cyc)=1)
   If pCarryA(cyc)>0 And iType(pCarryA(cyc))<10 And pSting(cyc)=0 Then DropItem(cyc,1)
   ChangeAnim(cyc,0)
  EndIf
  pSpeaking(cyc)=1
 EndIf 
 ;right LOW attack
 If pAnim(cyc)=23
  If pAnimTim(cyc)=0
   If pCarryB(cyc)=0 Then anim=14+(CarryState(cyc)*30)
   If pCarryB(cyc)>0 Then anim=(iAnim(iType(pCarryB(cyc)))+5)+(CarryState(cyc)*30)
   Animate p(cyc),3,2.5,pSeq(cyc,anim),5
   pSting(cyc)=1
  EndIf
  If pAnimTim(cyc)=3 Then ProduceSound(sAttack(charAttack(pType(cyc))),22050,0)
  If pAnimTim(cyc)=8 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>3 And pAnimTim(cyc)=<15 Then ApplyMovement(cyc,0.25)
  If pCarryB(cyc)>0 Then range=iRange(iType(pCarryB(cyc))) Else range=charRange(pType(cyc))
  If pAnimTim(cyc)=14 Then FindImpacts(cyc,range,2,1)
  If pAnimTim(cyc)=>25 Or (pAnimTim(cyc)=>20 And cAttackA(cyc)=1 And cAttackB(cyc)=0)
   If pCarryB(cyc)>0 And iType(pCarryB(cyc))<10 And pSting(cyc)=0 Then DropItem(cyc,2)
   ChangeAnim(cyc,0)
  EndIf
  pSpeaking(cyc)=1
 EndIf 
 ;left GROUND attack
 If pAnim(cyc)=24
  If pAnimTim(cyc)=0
   anim=17+(CarryState(cyc)*30)
   Animate p(cyc),3,2.5,pSeq(cyc,anim),5
   ResetBuilding(cyc) : pSting(cyc)=1
  EndIf
  If pAnimTim(cyc)=3 Then ProduceSound(sAttack(charAttack(pType(cyc))),22050,0)
  If pAnimTim(cyc)=8 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>3 And pAnimTim(cyc)=<15 Then ApplyMovement(cyc,0.25)
  If pAnimTim(cyc)=14 Then FindImpacts(cyc,4,1,3)
  If pAnimTim(cyc)=>25 Or (pAnimTim(cyc)=>20 And cAttackA(cyc)=0 And cAttackB(cyc)=1)
   If pCarryA(cyc)>0 And iType(pCarryA(cyc))<10 And pSting(cyc)=0 Then DropItem(cyc,1)
   ChangeAnim(cyc,0)
  EndIf
  pSpeaking(cyc)=1
 EndIf
 ;right GROUND attack
 If pAnim(cyc)=25
  If pAnimTim(cyc)=0
   anim=18+(CarryState(cyc)*30)
   Animate p(cyc),3,2.5,pSeq(cyc,anim),5
   ResetBuilding(cyc) : pSting(cyc)=1
  EndIf
  If pAnimTim(cyc)=3 Then ProduceSound(sAttack(charAttack(pType(cyc))),22050,0)
  If pAnimTim(cyc)=8 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>3 And pAnimTim(cyc)=<15 Then ApplyMovement(cyc,0.25)
  If pAnimTim(cyc)=14 Then FindImpacts(cyc,4,2,3)
  If pAnimTim(cyc)=>25 Or (pAnimTim(cyc)=>20 And cAttackA(cyc)=1 And cAttackB(cyc)=0)
   If pCarryB(cyc)>0 And iType(pCarryB(cyc))<10 And pSting(cyc)=0 Then DropItem(cyc,2)
   ChangeAnim(cyc,0)
  EndIf
  pSpeaking(cyc)=1
 EndIf  
 ;left FLYING attack
 If pAnim(cyc)=26
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,2.5,pSeq(cyc,123),5 : pSting(cyc)=1
  If pAnimTim(cyc)=5 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>8 And pAnimTim(cyc)=<15 Then FindImpacts(cyc,4,1,1)
  If pAnimTim(cyc)=>20 Then ChangeAnim(cyc,3)
  jumper#=pSpeed#(cyc)/2;pSpeed#(cyc)-(pSpeed#(cyc)/3)
  MoveEntity pPivot(cyc),0,0,jumper#/2
  ApplyMovement(cyc,jumper#)
  If pAnimTim(cyc)=>15 And pY#(cyc)<pGround#(cyc)+2 Then ChangeAnim(cyc,4)
  pSpeaking(cyc)=1
 EndIf 
 ;right FLYING attack
 If pAnim(cyc)=27
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,2.5,pSeq(cyc,124),5 : pSting(cyc)=1
  If pAnimTim(cyc)=5 Then ProduceSound(sSwing,22050,0)
  If pAnimTim(cyc)=>8 And pAnimTim(cyc)=<15 Then FindImpacts(cyc,4,2,1)
  If pAnimTim(cyc)=>20 Then ChangeAnim(cyc,3)
  jumper#=pSpeed#(cyc)/2;pSpeed#(cyc)-(pSpeed#(cyc)/3)
  MoveEntity pPivot(cyc),0,0,jumper#/2
  ApplyMovement(cyc,jumper#)
  If pAnimTim(cyc)=>15 And pY#(cyc)<pGround#(cyc)+2 Then ChangeAnim(cyc,4)
  pSpeaking(cyc)=1
 EndIf
 ;---------------------- PAIN & DEATH ------------------------------
 ;hurt (upper/lower)
 If pAnim(cyc)=30 Or pAnim(cyc)=31
  If pAnimTim(cyc)=0 And pAnim(cyc)=30 Then Animate p(cyc),3,Rnd(1.0,2.0),pSeq(cyc,130),3
  If pAnimTim(cyc)=0 And pAnim(cyc)=31 Then Animate p(cyc),3,Rnd(1.0,2.0),pSeq(cyc,131),3
  If pAnimTim(cyc)=1 Then ProduceSound(sPain(charPain(pType(cyc))),22050,1)
  If pAnimTim(cyc)=5 Then DropItem(cyc,1) : DropItem(cyc,2)
  If pAnimTim(cyc)<10 
   If pStagger#(cyc)<0.25 Then pStagger#(cyc)=0.25
   If pStagger#(cyc)>1.0 Then pStagger#(cyc)=1.0
   If pType(cyc)<10 Then MoveEntity pPivot(cyc),0,0,-pStagger#(cyc)
   If pType(cyc)=>10 Then MoveEntity pPivot(cyc),0,0,-(pStagger#(cyc)/2)
   pStepTim(cyc)=pStepTim(cyc)+1
  EndIf
  randy=Rnd(0,100)
  If (randy=0 And pAnimTim(cyc)=>0) Or pAnimTim(cyc)=>35 Then ChangeAnim(cyc,0)
  pSpeaking(cyc)=1
 EndIf
 ;collapse (human)
 If pAnim(cyc)=32 And pType(cyc)<10
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,2,pSeq(cyc,132),3
  If pAnimTim(cyc)=1 Then ProduceSound(sDeath(charDeath(pType(cyc))),22050,1)
  If pAnimTim(cyc)=5 Then DropItem(cyc,1) : DropItem(cyc,2)
  If pAnimTim(cyc)>2 And pAnimTim(cyc)<45 
   MoveEntity pPivot(cyc),0,0,-0.6
   pStepTim(cyc)=pStepTim(cyc)+1
  EndIf
  If pAnimTim(cyc)=36 Then ProduceSound(sFall,8000,1)
  If pAnimTim(cyc)=36 Then ScarBody(cyc,0,0,0)
  If pAnimTim(cyc)=>52
   ChangeAnim(cyc,33)
   pDT(cyc)=250-(pHealth(cyc)*2)
   If pHealth(cyc)<1 Then pHealth(cyc)=1
   pNowhere(cyc)=99
  EndIf
  pSpeaking(cyc)=1
 EndIf
 ;collapse (animal)
 If pAnim(cyc)=32 And pType(cyc)=>10
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,2,pSeq(cyc,132),3
  If pAnimTim(cyc)=1 Then ProduceSound(sDeath(charDeath(pType(cyc))),22050,1)
  If pAnimTim(cyc)>2 And pAnimTim(cyc)<25 
   MoveEntity pPivot(cyc),0,0,-0.3
   pStepTim(cyc)=pStepTim(cyc)+1
  EndIf
  If pAnimTim(cyc)=27 And charWidth(pType(cyc))=>8 Then ProduceSound(sFall,8000,1)
  If pAnimTim(cyc)=27 Then ScarBody(cyc,0,0,0)
  If pAnimTim(cyc)=>37 
   ChangeAnim(cyc,33)
   pDT(cyc)=250-(pHealth(cyc)*2)
   If pHealth(cyc)<1 Then pHealth(cyc)=1
   pNowhere(cyc)=99
  EndIf
 EndIf 
 ;lying
 If pAnim(cyc)=33
  If pAnimTim(cyc)=0 
   If pDT(cyc)>999 Then speeder#=0.05 Else speeder#=Rnd(0.1,0.3)
   Animate p(cyc),1,speeder#,pSeq(cyc,133),10
  EndIf
  If pDT(cyc)=<0 And (DirPressed(cyc) Or ActionPressed(cyc)) Then ChangeAnim(cyc,35)
  If game=>1 And pType(cyc)=1 And pHealth(cyc)=<1 And pDT(cyc)<999 Then ChangeAnim(cyc,39) 
  If pAnimTim(cyc)>20 Then SleepBenefits(cyc)
 EndIf
 ;lying hurt
 If pAnim(cyc)=34
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,Rnd(1.0,2.0),pSeq(cyc,134),3
  If pAnimTim(cyc)=1 Then ProduceSound(sPain(charPain(pType(cyc))),22050,1)
  If pType(cyc)=>10 Then getup=30 Else getup=20
  randy=Rnd(0,100)
  If (randy=0 And pAnimTim(cyc)=>10) Or pAnimTim(cyc)=>getup Then ChangeAnim(cyc,33)
  If pDT(cyc)=<0 And (DirPressed(cyc) Or ActionPressed(cyc)) Then ChangeAnim(cyc,33)
  pSpeaking(cyc)=1
 EndIf 
 ;get up (human)
 If pAnim(cyc)=35 And pType(cyc)<10
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,2,pSeq(cyc,135),5
  If pAnimTim(cyc)=5 Then ProduceSound(sShuffle,22050,1)
  If pAnimTim(cyc)=>52 
   ChangeAnim(cyc,0)
   If pHealth(cyc)<1 Then pHealth(cyc)=1
   pHP(cyc)=Rnd(1,charHP(pType(cyc)))
  EndIf
  pStepTim(cyc)=pStepTim(cyc)+1
  pSpeaking(cyc)=1
 EndIf
 ;get up (animal)
 If pAnim(cyc)=35 And pType(cyc)=>10
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,-2,pSeq(cyc,132),5
  If pAnimTim(cyc)=5 Then ProduceSound(sShuffle,22050,1)
  If pAnimTim(cyc)=>37 
   ChangeAnim(cyc,0)
   If pHealth(cyc)<1 Then pHealth(cyc)=1
   pHP(cyc)=Rnd(1,charHP(pType(cyc)))
  EndIf
  pStepTim(cyc)=pStepTim(cyc)+1
 EndIf
 ;go to sleep
 If pAnim(cyc)=36
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,2,pSeq(cyc,136),5
  If pAnimTim(cyc)=5 Then DropItem(cyc,1) : DropItem(cyc,2)
  If pAnimTim(cyc)<25 Then pStepTim(cyc)=pStepTim(cyc)+1
  If pAnimTim(cyc)=22 Then ProduceSound(sFall,8000,0.5)
  If pAnimTim(cyc)=>40 Then ChangeAnim(cyc,37)
  pSpeaking(cyc)=1
 EndIf
 ;sleeping
 If pAnim(cyc)=37
  If pAnimTim(cyc)=0 Then Animate p(cyc),1,Rnd(0.1,0.3),pSeq(cyc,137),5
  randy=Rnd(0,100)
  If randy=0 Then ProduceSound(sSnoring,8000,0)
  If DirPressed(cyc) Or ActionPressed(cyc)
   If pControl(cyc)>0 Then ChangeAnim(cyc,38)
   If pControl(cyc)=0 And (pHealth(cyc)=>70 Or gamTime(slot)<11000) Then ChangeAnim(cyc,38)
  EndIf
  SleepBenefits(cyc)
 EndIf
 ;wake up
 If pAnim(cyc)=38
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,-2,pSeq(cyc,136),5
  If pAnimTim(cyc)=5 Then ProduceSound(sShuffle,22050,1)
  If pAnimTim(cyc)=>40 Then ChangeAnim(cyc,0)
  pStepTim(cyc)=pStepTim(cyc)+1
  pSpeaking(cyc)=1
 EndIf
 ;death risk!
 If pAnim(cyc)=39
  If pAnimTim(cyc)=0 Then Animate p(cyc),3,0.7,pSeq(cyc,138),3 : camType=11
  If pAnimTim(cyc)=5 Then ProduceSound(sDying,22050,1)
  randy=Rnd(0,150)
  If gamSoul(slot)=2 Then randy=Rnd(0,140)
  If gamSoul(slot)=3 Then randy=Rnd(0,130)
  If randy=<1 And pAnimTim(cyc)=>70 And pAnimTim(cyc)=<160  
   ChangeAnim(cyc,35) : camType=camDefault
   If pHealth(cyc)<5 Then pHealth(cyc)=5
  EndIf
  If pAnimTim(cyc)>100 Then camType=12
  If pAnimTim(cyc)=>180 
   ChangeAnim(cyc,33) : pDT(cyc)=99999
   gamCheckpoint(slot,5)=1 : promoTim=0
  EndIf
  pSpeaking(cyc)=1
 EndIf 
 ;INCREMENT
 pAnimTim(cyc)=pAnimTim(cyc)+1
End Function

;--------------------------------------------------------------
;////////////////// RELATED FUNCTIONS /////////////////////////
;--------------------------------------------------------------

;CHANGE ANIMATION
Function ChangeAnim(cyc,newAnim)
 pOldAnim(cyc)=pAnim(cyc)
 pAnim(cyc)=newAnim
 If pOldAnim(cyc)>newAnim Then pAnimTim(cyc)=-1 Else pAnimTim(cyc)=0
End Function

;PLAYER ATTACK VIABLE?
Function AttackViable(cyc)
 ;standing viable by default
 viable=1
 ;ground viable
 If pAnim(cyc)=33 Then viable=3 ;lying
 If pAnim(cyc)=34 Then viable=3 ;hurt lying
 If pAnim(cyc)=37 Then viable=3 ;sleeping
 ;If charHeight(pType(cyc))<10 Then viable=3 ;small targets (rabbit)
 ;never viable
 If pAnim(cyc)=5 Then viable=0 ;riding boat
 If pAnim(cyc)=32 Or pAnim(cyc)=36 Then viable=0 ;collapsing
 If pAnim(cyc)=35 And pAnimTim(cyc)<40 Then viable=0 ;getting up
 If pAnim(cyc)=38 And pAnimTim(cyc)<30 Then viable=0 ;getting up (from sleep)
 If pAnim(cyc)=39 Then viable=0 ;dying
 If pState(cyc)=0 Then viable=0 ;not available
 If pIsland(cyc)<>gamIsland Then viable=0 ;not around
 ;return (1=high, 2=low, 3=ground) 
 Return viable 
End Function

;PLAYERS COLLISION VIABLE?
Function CollisionViable(cyc,v)
 viable=1
 If (pAnim(cyc)=2 Or pAnim(cyc)=3) And (pAnim(v)=33 Or pAnim(v)=37 Or pAnim(v)=39) 
  viable=0 ;allow jump over lying
 EndIf
 If pState(cyc)=0 Or pState(v)=0 Then viable=0 ;not available
 If pIsland(cyc)<>gamIsland Then viable=0 ;not around
 If cyc=v Then viable=0 ;nonsensical
 Return viable
End Function

;PLAYER COLLAPSE VIABLE?
Function CollapseViable(cyc)
 viable=1
 If pAnim(cyc)=3 Then viable=0 ;falling
 If pAnim(cyc)=5 Then viable=0 ;riding boat
 If pAnim(cyc)=32 Then viable=0 ;collapsing
 If pAnim(cyc)=33 Then viable=0 ;lying
 If pAnim(cyc)=34 Then viable=0 ;hurt lying
 If pAnim(cyc)=35 Then viable=0 ;getting up
 If pAnim(cyc)=37 Then viable=0 ;sleeping
 If pAnim(cyc)=38 Then viable=0 ;waking up
 If pAnim(cyc)=39 Then viable=0 ;dying
 If pState(cyc)=0 Then viable=0 ;not available
 Return viable
End Function

;PLAYER FALL VIABLE?
Function FallViable(cyc)
 viable=1
 If pAnim(cyc)=2 Then viable=0 ;jumping
 If pAnim(cyc)=3 Then viable=0 ;flying
 If pAnim(cyc)=5 Then viable=0 ;riding boat
 If pAnim(cyc)=26 Then viable=0 ;fly attack (left)
 If pAnim(cyc)=27 Then viable=0 ;fly attack (right)
 If pAnim(cyc)=37 Then viable=0 ;sleeping 
 If pY#(cyc)=<optSeaLevel# Then viable=0 ;underwater
 If pState(cyc)=0 Then viable=0 ;not available
 Return viable
End Function