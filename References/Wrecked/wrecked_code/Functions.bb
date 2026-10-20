;//////////////////////////////////////////////////////////////////////////////
;---------------------------- WRECKED: FUNCTIONS ------------------------------
;//////////////////////////////////////////////////////////////////////////////

;PRODUCE VARIANT SOUND
Function ProduceSound(sound,pitch,vol#)
 ;vary pitch & volume
 range1=pitch-(pitch/8)
 range2=pitch+(pitch/12)
 If pitch=44000
  If range1<41000 Then range1=41000
  If range2>45500 Then range2=45500
 EndIf
 pitcher=Rnd(range1,range2)
 If vol#=0 Then vol#=Rnd(0.4,1.2)
 ;deliver sound
 If sound>0
  SoundPitch sound,pitcher
  SoundVolume sound,vol#
  PlaySound sound
  ;reset
  SoundPitch sound,pitch
  SoundVolume sound,1
 EndIf
End Function

;///////////////////////////// GRAPHICAL ///////////////////////////////////

;OUTLINE
Function Outline(script$,x,y,r1,g1,b1,r2,g2,b2)
 Color r1,g1,b1
 Text x+1,y,script$,1,1
 Text x-1,y,script$,1,1
 Text x,y+1,script$,1,1
 Text x,y-1,script$,1,1
 Color r2,g2,b2
 Text x,y,script$,1,1
End Function

;OUTLINE STRAIGHT
Function OutlineStraight(script$,x,y,r1,g1,b1,r2,g2,b2)
 Color r1,g1,b1
 Text x+1,y,script$,0,1
 Text x-1,y,script$,0,1
 Text x,y+1,script$,0,1
 Text x,y-1,script$,0,1
 Color r2,g2,b2
 Text x,y,script$,0,1
End Function

;CHANGE RESOLUTION
Function ChangeRes(task) ;0=for menu, 1=for game
 ;new graphical mode
 ;Loader("PLEASE WAIT","ADJUSTING RESOLUTION")
 If task=0 
  Graphics3D 800,600,16,1
  currRes=2
 Else
  If optRes=0 Then Graphics3D 320,240,16,1
  If optRes=1 Then Graphics3D 640,480,16,1
  If optRes=2 Then Graphics3D 800,600,16,1
  If optRes=3 Then Graphics3D 1024,768,16,1
  currRes=optRes
 EndIf
 ;restore media
 LoadImages()
 LoadTextures()
End Function

;RESOLUTION X FIX
Function rX#(value#)
 orig#=800.0
 If currRes=0 Then factor#=orig#/320.0
 If currRes=1 Then factor#=orig#/640.0
 If currRes=3 Then factor#=orig#/1024.0
 If currRes=4 Then factor#=orig#/1280.0
 If currRes=2 Then newX#=value# Else newX#=value#/factor#
 Return newX#
End Function

;RESOLUTION Y FIX
Function rY#(value#)
 orig#=600.0
 If currRes=0 Then factor#=orig#/240.0
 If currRes=1 Then factor#=orig#/480.0
 If currRes=3 Then factor#=orig#/768.0
 If currRes=4 Then factor#=orig#/1024.0
 If currRes=2 Then newY#=value# Else newY#=value#/factor#
 Return newY#
End Function

;///////////////////////////// MATHEMATICAL ///////////////////////////////////

;CALCULATE 1'000'000 FIGURE
Function GetFigure$(value)
  minus=0
  If value<0 Then minus=1 : value=value-(value*2)
  ;get segments
  hundreds=0 : thousands=0 : millions=0
  millions=value/1000000
  If millions<0 Then millions=0
  thousands=(value-(millions*1000000))/1000
  If thousands<0 Then thousands=0
  hundreds=value-((millions*1000000)+(thousands*1000))
  If hundreds<0 Then hundreds=0
  ;piece together
  hun$=hundreds
  If thousands>0 Then tho$=thousands Else tho$=""
  If millions>0 Then mil$=millions Else mil$=""
  If (thousands>0 Or millions>0) Then hun$="'"+hundreds
  If (thousands>0 Or millions>0) And hundreds<100 Then hun$="'0"+hundreds
  If (thousands>0 Or millions>0) And hundreds<10 Then hun$="'00"+hundreds
  If millions>0 Then tho$="'"+thousands
  If millions>0 And thousands<100 Then tho$="'0"+thousands
  If millions>0 And thousands<10 Then tho$="'00"+thousands
  ;return
  If minus=0 Then figure$=mil$+tho$+hun$ 
  If minus=1 Then figure$="-"+mil$+tho$+hun$
  Return figure$
End Function

;TRANSLATE NUMBER INTO DIGITS
Function Dig$(value,degree)
 digits$=value
 If value<degree Then digits$="0"+digits$
 If value<degree/10 Then digits$="0"+digits$
 If value=0 And degree=10 Then digits$="00"
 If value=0 And degree=100 Then digits$="000"
 Return digits$
End Function

;GET FIGURE TO NEAREST ???
Function RoundOff(value,degree)
 floater#=value/degree
 inty=Int(floater#)
 returner=inty*degree
 Return returner
End Function 

;CLEAN GIVEN ANGLE
Function CleanAngle#(angle#)
 Repeat
  If angle#<0 Then angle#=angle#+360
  If angle#>360 Then angle#=angle#-360
 Until angle#=>0 And angle#=<360
 Return angle#
End Function

;FIND BEST ANGLE ROUTE
Function ReachAngle#(sA#,tA#,speed#)
 If sA#<tA# Then value#=speed# Else value#=-speed#
 If sA#>270 And tA#<90 Then value#=speed#
 If sA#<90 And tA#>270 Then value#=-speed#
 Return value#
End Function

;CALCULATE DIFFERENCE (between any 2 numbers)
Function GetDiff#(source#,dest#)
 diff#=dest#-source#
 If diff#<0 Then diff#=diff#-(diff#*2) 
 Return diff#
End Function