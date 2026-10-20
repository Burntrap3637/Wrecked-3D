;//////////////////////////////////////////////////////////////////////////////
;------------------------- WRECKED: PROMOS & MISSIONS -------------------------
;//////////////////////////////////////////////////////////////////////////////

;--------------------------------------------------------------------------
;///////////////////////////// SUBTITLES //////////////////////////////////
;--------------------------------------------------------------------------
Function DisplayPromo()
 ;subtitle background
 Color 0,0,0
 Rect 0,0,1024,rY#(80),1
 Rect 0,rY#(480),1024,300,1
 ;prepare fonts
 SetFont font(3) 
 If currRes=<1 Then SetFont font(2)
 r1=50 : g1=50 : b1=50
 r2=250 : g2=250 : b2=250
 x=rX#(400) : y=rY#(515) : s=rY#(40)
 ;~~~~~~~~~~~~~~~~~~~~ 1. SOLDIER'S THOUGHTS ~~~~~~~~~~~~~~~~~~~~~~~~
 If promo=1
  If promoTim>5 And promoTim<350
   Speak(33,0,1)
   Outline("'Shuttle'... 'They Will Mourn'? Damn, these carvings",x,y,r1,g1,b1,r2,g2,b2)
   Outline("don't make no sense! This whole place is messed up!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>360 And promoTim<710
   Speak(33,1,3)
   Outline("Must mean something though. Otherwise that white-haired",x,y,r1,g1,b1,r2,g2,b2)
   Outline("fool wouldn't have gone mad and run away to the North...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>720 And promoTim<1070
   Speak(33,1,2)
   Outline("He can't look at that blue one without crying his ass off!",x,y,r1,g1,b1,r2,g2,b2)
   Outline("Says he feels the pain of a 'million souls' or something?",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1080 And promoTim<1430
   Speak(33,1,1)
   Outline("Don't listen to what he says anyway. Old fool called me a",x,y,r1,g1,b1,r2,g2,b2)
   Outline("'butcher' the minute he saw me! What's that about?!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1440 And promoTim<1790
   Speak(33,1,1)
   Outline("You try to restore order to this God-forsaken place",x,y,r1,g1,b1,r2,g2,b2)
   Outline("and THAT'S the thanks you get?! Stupid hippies...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(33,1,1)
   Outline("Makes me wish i had some bullets left in this gun, so",x,y,r1,g1,b1,r2,g2,b2)
   Outline("i could put one in his head! Hell, i'll kill anybody...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 And promoTim<2510
   Speak(33,1,1)
   Outline("That goes for you too, son. Don't get in my way when i'm",x,y,r1,g1,b1,r2,g2,b2)
   Outline("handling my business. Cats get real savage out here...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2520 Then gamCheckpoint(slot,1)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;~~~~~~~~~~~~~~~~~~~~~~~~~~ MYSTIC INTROS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 If promo=>10 And promo<50
  ;thief variation
  If gamSoul(slot)=<2 And gamSins(slot,gamCrime(slot))=1
   If promoTim>5 And promoTim<350
    Speak(36,0,1)
    Outline("The great thief comes before me! The owner of all things!",x,y,r1,g1,b1,r2,g2,b2)
    Outline("What's yours is yours... and what's OURS is yours too?!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,1)
    Outline("Come, as my people go hungry, they wonder why you felt",x,y,r1,g1,b1,r2,g2,b2)
    Outline("the need to invade their pen and steal their produce?",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,1)
    Outline("There is no answer, because it was an evil spirit that",x,y,r1,g1,b1,r2,g2,b2)
    Outline("loomed over your actions! Cursed are those hands...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 And promoTim<1430
    Speak(36,0,2)
    Outline("But i say to you, redeem yourself before the Creator",x,y,r1,g1,b1,r2,g2,b2)
    Outline("so that you may avoid the wrath of HIS mighty hand!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
  EndIf
  ;beastial variation
  If gamSoul(slot)=<2 And gamSins(slot,gamCrime(slot))=2
   If promoTim>5 And promoTim<350
    Speak(36,0,1)
    Outline("The butcher of animals comes before me! A man reveals",x,y,r1,g1,b1,r2,g2,b2)
    Outline("himself in how he treats the most lowly of creatures...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,1)
    Outline("Let us agree that it is a most sinister force that",x,y,r1,g1,b1,r2,g2,b2)
    Outline("governs your actions! Savage are those instincts...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,1)
    Outline("All animals are carved by the Creator's loving hand.",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Why, then, do you abuse and destroy his good work?!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 And promoTim<1430
    Speak(36,0,2)
    Outline("I say to you, humble yourself before all creatures.",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Only then will you receive the mercy that you lack...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
  EndIf
  ;murder variation
  If gamSoul(slot)=<2 And gamSins(slot,gamCrime(slot))=3
   If promoTim>5 And promoTim<350
    Speak(36,0,1)
    Outline("The butcher of men comes before me! Foul is your sin,",x,y,r1,g1,b1,r2,g2,b2)
    Outline("and most deafening are the screams of your victims!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,1)
    Outline("The Creator made man in his own image, and yet",x,y,r1,g1,b1,r2,g2,b2)
    Outline("you tear through flesh as if you were HE!!!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>720 And promoTim<1070
    Speak(36,0,1)
    Outline("How do you intend to leave this island when",x,y,r1,g1,b1,r2,g2,b2)
    Outline("you taunt the very CREATOR of the waves?!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 And promoTim<1430
    Speak(36,0,2)
    Outline("I say to you, reconcile yourself with the Creator",x,y,r1,g1,b1,r2,g2,b2)
    Outline("so that his waters may take mercy on your plight...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
  EndIf
  ;divine variation
  If gamSoul(slot)=3 Or gamCrime(slot)=0
   If promoTim>5 And promoTim<350
    Speak(36,0,3)
    Outline("The great visitor comes before me! Humbled am i",x,y,r1,g1,b1,r2,g2,b2)
    Outline("to assist a man with a destiny such as yours!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,3)
    Outline("Your heart cries to leave this island, and it is",x,y,r1,g1,b1,r2,g2,b2)
    Outline("a great spirit that presides over your fate...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>720 And promoTim<1070
    Speak(36,0,3)
    Outline("The Creator is most please with your efforts!",x,y,r1,g1,b1,r2,g2,b2)
    Outline("His waters are waiting to guide you to safety...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 And promoTim<1430
    Speak(36,0,2)
    Outline("However, you are here for a most divine purpose.",x,y,r1,g1,b1,r2,g2,b2)
    Outline("There are still many lessons that you must learn...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
  EndIf 
 EndIf
 ;~~~~~~~~~~~~~~~~~~~~~~~~~~ MYSTIC ASSIGNMENTS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ;1. deliver food
 If promo=11
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("The gravest of sins make a man affluent, but i say",x,y,r1,g1,b1,r2,g2,b2)
   Outline("to you: cursed is the life that serves itself!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,3)
   Outline("The Creator's people go hungry. Praised is the",x,y,r1,g1,b1,r2,g2,b2)
   Outline("man that returns to me with arms full of food...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;2. deliver vines
 If promo=12
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("The gravest of sins make a man affluent, but i say",x,y,r1,g1,b1,r2,g2,b2)
   Outline("to you: cursed is the life that serves itself!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,3)
   Outline("My people go without vines to work with. Praised",x,y,r1,g1,b1,r2,g2,b2)
   Outline("is the man that delivers these materials to us...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;3. deliver wood
 If promo=13
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Many men sin for material gain, but i say to you:",x,y,r1,g1,b1,r2,g2,b2)
   Outline("store up SPIRITUAL riches that will never perish!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,3)
   Outline("My people go without wood to build with. Praised is",x,y,r1,g1,b1,r2,g2,b2)
   Outline("the man that returns to me with arms full of timber...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;4. deliver stone
 If promo=14
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Many men sin for material gain, but i say to you:",x,y,r1,g1,b1,r2,g2,b2)
   Outline("store up SPIRITUAL riches that will never perish!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,3)
   Outline("My people struggle for precious stone. Praised is the",x,y,r1,g1,b1,r2,g2,b2)
   Outline("man that toils before a rock and delivers it to us...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;5. deliver hammer
 If promo=15
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Ignorant men ask, 'What is the meaning of life?'",x,y,r1,g1,b1,r2,g2,b2)
   Outline("I say to you: the meaning of creation is to CREATE!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("Turn your imagination to the fruits of the island,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("and bring forth a fine hammer to honour your Master...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;6. deliver spear
 If promo=16
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Ignorant men ask, 'What is the meaning of life?'",x,y,r1,g1,b1,r2,g2,b2)
   Outline("I say to you: the meaning of creation is to CREATE!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("Turn your imagination to the fruits of the island,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("and bring forth a fine spear to honour your Master...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;7. deliver axe
 If promo=17
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Ignorant men ask, 'What is the meaning of life?'",x,y,r1,g1,b1,r2,g2,b2)
   Outline("I say to you: the meaning of creation is to CREATE!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("Turn your imagination to the fruits of the island,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("and bring forth a fine axe to honour your Master...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;8. deliver gun
 If promo=18
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("You are not the first visitor to this island.",x,y,r1,g1,b1,r2,g2,b2)
   Outline("Foul butchers came forth with the Devil's Metal!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("He who lives amongst such evil shall die by it!",x,y,r1,g1,b1,r2,g2,b2)
   Outline("Seize the offending weaponry and bring it before me...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;9. deliver artefact
 If promo=19
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Cursed is the man that wastes the gift of life.",x,y,r1,g1,b1,r2,g2,b2)
   Outline("Don't live in fear of the world - EXPLORE it!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("Most praised is the adventurer that finds the sacred",x,y,r1,g1,b1,r2,g2,b2)
   Outline("artefact and brings its Heavenly glow before us...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;10. sacrifice flesh
 If promo=20
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Many of man's sins are designed to preserve his body,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("but i say to you: the flesh is not yours to shield!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("Sacrifice it to the Creator from whence it came! Expose",x,y,r1,g1,b1,r2,g2,b2)
   Outline("your body to the world and return with the scars of a man...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;11. go without sleep
 If promo=21
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("A man's sins are designed to make life easier,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("but i say to you: EMBRACE HARDSHIP!!!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("Who are you to avoid the struggle of life?! Return to",x,y,r1,g1,b1,r2,g2,b2)
   Outline("me with a weary body and may your efforts refresh you...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;12. go without food
 If promo=22
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("A man's sins are often intended to fill his stomach,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("but i say to you: gluttony is a sin in itself!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("Find nourishment in the Heavens - not the Earth! Return",x,y,r1,g1,b1,r2,g2,b2)
   Outline("to me with an empty stomach and be truly fulfilled...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;13. go without water
 If promo=23
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Cursed are those that sit by the coast and drink all day!",x,y,r1,g1,b1,r2,g2,b2)
   Outline("They shall never drink from the Creator's fountain!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("Sacrifice your mouth to HIM, so that HE may quench it!",x,y,r1,g1,b1,r2,g2,b2)
   Outline("Return to me with a thirst and let it replenish you...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;14. kill all cats
 If promo=24
  If promoTim>1440 And promoTim<1790
   Speak(36,0,2)
   Outline("The Creator's infinite mercy looks kindly on wise men,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("but his world also needs WARRIORS to keep it turning!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,3)
   Outline("My people live in fear of the Devil's Cats! Praised",x,y,r1,g1,b1,r2,g2,b2)
   Outline("is the man that slays them all and restores peace...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;15. kill all dogs
 If promo=25
  If promoTim>1440 And promoTim<1790
   Speak(36,0,2)
   Outline("The Creator's infinite mercy looks kindly on wise men,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("but his world also needs WARRIORS to keep it turning!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,3)
   Outline("My people live in fear of the Devil's Hounds! Praised",x,y,r1,g1,b1,r2,g2,b2)
   Outline("is the man that slays them all and restores peace...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;16. light fires
 If promo=26
  If promoTim>1440 And promoTim<1790
   Speak(36,0,1)
   Outline("Men of little faith ask, 'Where is the Creator?'",x,y,r1,g1,b1,r2,g2,b2)
   Outline("I say to you: his energy is felt all around!",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1800 And promoTim<2150
   Speak(36,0,2)
   Outline("My people go cold on these shores. Light a fire in",x,y,r1,g1,b1,r2,g2,b2)
   Outline("every land and let the Creator's energy shine forth...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>2160 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault 
 EndIf
 ;~~~~~~~~~~~~~~~~~~~~~~~~~~ MYSTIC VERDICTS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 If promo=50
  ;evil variation
  If gamSoul(slot)=1
   If promoTim>5 And promoTim<350
    Speak(36,0,2)
    Outline("The wicked man humbles himself before the Creator?",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Your gesture is encouraging, but your sins are deeper...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,1)
    Outline("Time and time again must you honour your Master!",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Your good deeds should be as infinite as he...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
  EndIf
  ;flawed variation
  If gamSoul(slot)=2
   If promoTim>5 And promoTim<350
    Speak(36,0,3)
    Outline("You honour the Creator with your selfless actions!",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Forgiven is the man that learns from his mistakes...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,2)
    Outline("The righteous path is beneath your feet, but i say to",x,y,r1,g1,b1,r2,g2,b2)
    Outline("you: KEEP WALKING! There is much more to learn...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
  EndIf
  ;divine variation
  If gamSoul(slot)=3
   If promoTim>5 And promoTim<350
    Speak(36,0,3)
    Outline("Behold the great student! Great is the man without sin,",x,y,r1,g1,b1,r2,g2,b2)
    Outline("but greater still is the man of infinite compassion!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,3)
    Outline("While others build boats of wood, i assure you that",x,y,r1,g1,b1,r2,g2,b2)
    Outline("you are building a most fine boat of the Heavens!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
  EndIf
  ;generic ending
  If promoTim>720 And promoTim<1070
   Speak(36,0,2)
   Outline("Allow me to meditate on your next lesson. Until then,",x,y,r1,g1,b1,r2,g2,b2)
   Outline("go forth and pray that your spirit remains intact...",x,y+s,r1,g1,b1,r2,g2,b2)
  EndIf
  If promoTim>1080 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault
 EndIf
 ;~~~~~~~~~~~~~~~~~~~~~~~~~~ MYSTIC FAREWELLS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 If promo=60
  ;evil variation
  If gamSoul(slot)=1
   If promoTim>5 And promoTim<350
    Speak(36,0,1)
    Outline("Truly, it is a most wicked man that washed up on these",x,y,r1,g1,b1,r2,g2,b2)
    Outline("shores. The darkest of spirits lived in your actions...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,1)
    Outline("AWAY WITH YOU! Take your evil form to the coast,",x,y,r1,g1,b1,r2,g2,b2)
    Outline("and build a vessel with your ill-gotten gains...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,1)
    Outline("But know this: everything i warned of was true.",x,y,r1,g1,b1,r2,g2,b2)
    Outline("The Creator will not take mercy on your journey...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault
  EndIf
  ;flawed variation
  If gamSoul(slot)=2
   If promoTim>5 And promoTim<350
    Speak(36,0,2)
    Outline("Come, there is no more i can teach you. The spiritual",x,y,r1,g1,b1,r2,g2,b2)
    Outline("journey is complete, and now the physical one begins!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,2)
    Outline("Go forth to the coast and build a boat of wood and vine.",x,y,r1,g1,b1,r2,g2,b2)
    Outline("May it carry you to a happier destiny than this one...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,2)
    Outline("You made mistakes on this island, but you are redeemed.",x,y,r1,g1,b1,r2,g2,b2)
    Outline("I assure you that the Creator will take mercy on you...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault
  EndIf
  ;divine variation
  If gamSoul(slot)=3
   If promoTim>5 And promoTim<350
    Speak(36,0,3)
    Outline("Sad am i that we must bid farewell to the greatest man",x,y,r1,g1,b1,r2,g2,b2)
    Outline("that ever walked upon these shores. Your work is done!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,3)
    Outline("I have taught you all that this island has to teach,",x,y,r1,g1,b1,r2,g2,b2)
    Outline("and now you must deliver your wisdom to other lands...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,3)
    Outline("Go forth to the coast and build a boat of wood and vine.",x,y,r1,g1,b1,r2,g2,b2)
    Outline("The Creator will usher you to safety with a loving hand...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 Then gamCheckpoint(slot,2)=1 : promo=0 : camFoc(1)=1 : camType=camDefault
  EndIf
 EndIf
 ;~~~~~~~~~~~~~~~~~~~~~~~~~~ MYSTIC ENDINGS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 If promo=70
  ;evil variation
  If gamSoul(slot)=1
   If promoTim>5 And promoTim<350
    Speak(36,0,1)
    Outline("You delivered a most evil man to these shores.",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Quite what you expected from him is beyond me...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,1)
    Outline("I say to you: his disregard for life, of any kind,",x,y,r1,g1,b1,r2,g2,b2)
    Outline("was below that of even the most flawed mortal!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,1)
    Outline("These primitive conditions excited an evil mind",x,y,r1,g1,b1,r2,g2,b2)
    Outline("instead of humbling a divine soul...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 And promoTim<1430
    Speak(36,0,1)
    Outline("He lived without your guidance, so i say to you:",x,y,r1,g1,b1,r2,g2,b2)
    Outline("let him DIE without your guidance!!!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1440 And promoTim<1790
    Speak(36,0,1)
    Outline("Sink his ill-gotten craft, and may his evil form",x,y,r1,g1,b1,r2,g2,b2)
    Outline("never again reach the shores of righteous men...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1800 Then gamCheckpoint(slot,5)=1 : promo=0 : promoTim=0 : camType=14
  EndIf
  ;ignorant variation
  If gamSoul(slot)=>2 And CountMissions()<10
   If promoTim>5 And promoTim<350
    Speak(36,0,1)
    Outline("Why does the great visitor leave the island?!",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Come, there was much that he had left to learn!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,1)
    Outline("I spoke of potential in the man, and we all saw it,",x,y,r1,g1,b1,r2,g2,b2)
    Outline("but he was not instructed to leave at this hour!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,1)
    Outline("Has he thrown away this most sacred of destinies?!",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Then let his haste speak about his character!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 And promoTim<1430
    Speak(36,0,1)
    Outline("Come, an ignorant man could never be the one!",x,y,r1,g1,b1,r2,g2,b2)
    Outline("I was instructed to look for the great student...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1440 And promoTim<1790
    Speak(36,0,1)
    Outline("Let the ignorant man follow his own destiny, and may",x,y,r1,g1,b1,r2,g2,b2)
    Outline("he resume the mortal life that he craves so badly...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1800 Then gamCheckpoint(slot,5)=1 : promo=0 : promoTim=0 : camType=14
  EndIf
  ;flawed variation
  If gamSoul(slot)=2 And CountMissions()=>10
   If promoTim>5 And promoTim<350
    Speak(36,0,2)
    Outline("You delivered a great man to this island - but let us",x,y,r1,g1,b1,r2,g2,b2)
    Outline("dwell on the word 'man'. Come, he was not the one...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,2)
    Outline("He survived this most merciless destiny with his spirit",x,y,r1,g1,b1,r2,g2,b2)
    Outline("intact, but not without revealing the flaws of a mortal!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,2)
    Outline("Was i not instructed to look for the man without sin?",x,y,r1,g1,b1,r2,g2,b2)
    Outline("Come, this man was acting out of fear - not compassion!",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 And promoTim<1430
    Speak(36,0,2)
    Outline("His desire to return to civilization was strong, and",x,y,r1,g1,b1,r2,g2,b2)
    Outline("fuelled his better moments, so let that be his destiny...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1440 And promoTim<1790
    Speak(36,0,2)
    Outline("I say to you: return him to the life that he craved,",x,y,r1,g1,b1,r2,g2,b2)
    Outline("and may he live out the rest of his days in peace...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1800 Then gamCheckpoint(slot,5)=1 : promo=0 : promoTim=0 : camType=14
  EndIf
  ;divine variation
  If gamSoul(slot)=3 And CountMissions()=>10
   If promoTim>5 And promoTim<350
    Speak(36,0,3)
    Outline("The island rejoices! After years of seperating the wheat",x,y,r1,g1,b1,r2,g2,b2)
    Outline("from the chaff, we finally discover the greatest of men...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>360 And promoTim<710
    Speak(36,0,3)
    Outline("You delivered him to me in the most dire circumstances,",x,y,r1,g1,b1,r2,g2,b2)
    Outline("but never once did he burden others with his plight...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf 
   If promoTim>720 And promoTim<1070
    Speak(36,0,3)
    Outline("Come, he did the exact opposite! He accepted his destiny",x,y,r1,g1,b1,r2,g2,b2)
    Outline("and brought peace to my people with each lesson learned...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1080 And promoTim<1430
    Speak(36,0,3)
    Outline("Behold the diamonds made from extreme pressure! His ",x,y,r1,g1,b1,r2,g2,b2)
    Outline("spirit never faultered from that of a truly divine man...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1440 And promoTim<1790
    Speak(36,0,3)
    Outline("I have met my destiny - now let him meet his! Guide him",x,y,r1,g1,b1,r2,g2,b2)
    Outline("safely to other shores and let the world know its new King...",x,y+s,r1,g1,b1,r2,g2,b2)
   EndIf
   If promoTim>1800 Then gamCheckpoint(slot,5)=1 : promo=0 : promoTim=0 : camType=14
  EndIf
 EndIf
End Function

;--------------------------------------------------------------
;////////////////// RELATED FUNCTIONS /////////////////////////
;--------------------------------------------------------------

;SWITCH SPEAKERS
Function Speak(cyc,v,expression)
 ;focus on speaker
 pSpeaking(cyc)=1
 If game=<1 Then camFoc(1)=cyc : camType=10
 ;face each other
 If v>0
  PointEntity p(cyc),p(v)
  pTA#(cyc)=EntityYaw(p(cyc))
  pA#(cyc)=pTA#(cyc)
  PointEntity p(v),p(cyc)
  pTA#(v)=EntityYaw(p(v)) 
  pA#(v)=pTA#(v)
 EndIf
 ;chnage expression
 If expression>0 Then pEyes(cyc)=expression
End Function

;FACIAL EXPRESSIONS
Function FacialExpressions(cyc)
 If pType(cyc)<10
  ;eye expressions
  If pEyes(cyc)<>pOldEyes(cyc) And pEyes(cyc)>0
   EntityTexture pLimb(cyc,1),tEyes(pEyes(cyc)),0,2
   pOldEyes(cyc)=pEyes(cyc)
  EndIf
  ;mouth expressions
  If pSpeaking(cyc)=1
   randy=Rnd(0,3)
   If randy=0 Then pMouth(cyc)=Rnd(-2,5)
   If pMouth(cyc)<0 Then pMouth(cyc)=0
   EntityTexture pLimb(cyc,1),tMouth(pMouth(cyc)),0,3
  EndIf
  If pSpeaking(cyc)=0 And pMouth(cyc)>0
   pMouth(cyc)=0
   EntityTexture pLimb(cyc,1),tMouth(0),0,3
  EndIf
 EndIf
End Function

;CHECK ACTORS ARE NEARBY
Function PromoVicinity(cyc,v,range,style) ;0=any animation, 1=only when still
 value=0
 If pIsland(v)=gamIsland And pState(v)>0
  If pX#(cyc)>pX#(v)-range And pX#(cyc)<pX#(v)+range And pZ#(cyc)>pZ#(v)-range And pZ#(cyc)<pZ#(v)+range And pY#(cyc)>pY#(v)-20 And pY#(cyc)<pY#(v)+20
   If style=0 Then value=1
   If style=1 And pAnim(cyc)=<1 And pAnim(v)=0 Then value=1
  EndIf
 EndIf
 Return value
End Function

;FIND PROMOS & ASSESS STATUS
Function FindPromos(cyc)
 ;increment script
 promoTim=promoTim+1
 ;negate lengthy mission
 If gamMission(slot)>0 And gamDay(slot)>gamMissionDay(slot)+2
  gamResult(slot,gamMission(slot))=-1 
  gamMission(slot)=0
 EndIf
 ;acknowledge play has started
 If pAnim(cyc)=<1 And pDT(cyc)=<0 Then gamCheckpoint(slot,4)=1 
 ;TRIGGER PROMOS
 If promo=0 And game=>1
  ;soldier intro
  If pX#(cyc)>-310 And PromoVicinity(cyc,33,30,1) And gamCheckpoint(slot,1)=0 
   promo=1 : promoTim=0
  EndIf
  ;assign mission
  If gamMission(slot)=0 And PromoVicinity(cyc,36,30,1) And gamCheckpoint(slot,2)=0 And MissionsLeft()>0 And CountCrimes()<10
   AssignMission()
   gamMissionDay(slot)=gamDay(slot)
   promo=10+gamMission(slot) : promoTim=0
  EndIf
  ;complete mission
  If promo=0 And gamMission(slot)>0 And PromoVicinity(cyc,36,30,1)
   done=0
   If gamMission(slot)=1 And (iType(pCarryA(cyc))=4 Or iType(pCarryA(cyc))=5) And (iType(pCarryB(cyc))=4 Or iType(pCarryB(cyc))=5)
    EmptyHands(cyc,0) : done=1 ;deliver food
   EndIf
   If gamMission(slot)=2 And iType(pCarryA(cyc))=1 And iType(pCarryB(cyc))=1 
    EmptyHands(cyc,0) : done=1 ;bring vines
   EndIf
   If gamMission(slot)=3 And iType(pCarryA(cyc))=2 And iType(pCarryB(cyc))=2
    EmptyHands(cyc,0) : done=1 ;bring wood
   EndIf
   If gamMission(slot)=4 And iType(pCarryA(cyc))=3 And iType(pCarryB(cyc))=3
    EmptyHands(cyc,0) : done=1 ;bring stone
   EndIf
   If gamMission(slot)=5 And (iType(pCarryA(cyc))=13 Or iType(pCarryB(cyc))=13) 
    EmptyHands(cyc,13) : done=1 ;bring hammer
   EndIf
   If gamMission(slot)=6 And (iType(pCarryA(cyc))=14 Or iType(pCarryB(cyc))=14)
    EmptyHands(cyc,14) : done=1 ;bring spear
   EndIf
   If gamMission(slot)=7 And (iType(pCarryA(cyc))=15 Or iType(pCarryB(cyc))=15)
    EmptyHands(cyc,15) : done=1 ;bring axe
   EndIf
   If gamMission(slot)=8 And (iType(pCarryA(cyc))=16 Or iType(pCarryB(cyc))=16)
    EmptyHands(cyc,16) : done=1 ;bring gun
   EndIf
   If gamMission(slot)=9 And (iType(pCarryA(cyc))=17 Or iType(pCarryB(cyc))=17)
    EmptyHands(cyc,17) : done=1 ;bring artefact
   EndIf
   If gamMission(slot)=10 And CountScars(cyc)=>4 Then done=1 ;sacrifice flesh
   If gamMission(slot)=11 And pHealth(cyc)=<20 Then done=1 ;go without sleep
   If gamMission(slot)=12 And pHunger(cyc)=>80 Then done=1 ;go without food
   If gamMission(slot)=13 And pThirst(cyc)=>80 Then done=1 ;go without water
   If gamMission(slot)=14 And CountAnimals(10)=0 Then done=1 ;slay cats
   If gamMission(slot)=15 And CountAnimals(11)=0 Then done=1 ;slay dogs
   If gamMission(slot)=16 And fireState(1)>0 And fireState(2)>0 And fireState(3)>0 And fireState(4)>0 And fireState(5)>0 
    done=1 ;light fires
   EndIf
   If done=1 ;register completion
    If gamCrime(slot)>0 Then gamSins(slot,gamCrime(slot))=-1
    gamResult(slot,gamMission(slot))=1
    gamMission(slot)=0
    promo=50 : promoTim=0
   EndIf
  EndIf
  ;mission reminder
  If promo=0 And gamMission(slot)>0 And PromoVicinity(cyc,36,30,1) And gamCheckpoint(slot,2)=0
   promo=10+gamMission(slot) : promoTim=1435
  EndIf
  ;acknowledge lost causes
  If PromoVicinity(cyc,36,30,1) And gamCheckpoint(slot,2)=0 And (CountMissions()=>10 Or CountCrimes()=>10)
   promo=60 : promoTim=0
  EndIf
  ;ending
  If game=2 And gamCheckpoint(slot,5)=0
   If gotim>200 Then camType=13
   If gotim>450 Then promo=70 : promoTim=0
  EndIf
 EndIf
 ;UPDATE STATUS
 ;assess status
 AssessStatus()
 ;check for theft!
 If gamCheckpoint(slot,3)=0
  For count=1 To 5
   item=300+(count*8)
   If iState(item)=0 Or iX#(item)>-180 Or iZ#(item)<470 And gamCheckpoint(slot,3)=0
    AddCrime(1) : gamCheckpoint(slot,3)=1
   EndIf
  Next
 EndIf
 ;find latest crime
 FindCrime()
End Function

;ASSESS STATUS
Function AssessStatus()
 If CountCrimes()-CountMissions()>0 Then gamSoul(slot)=1 ;evil
 If CountCrimes()-CountMissions()=<0 Then gamSoul(slot)=2 ;flawed
 If CountCrimes()=0 Then gamSoul(slot)=3 ;divine 
End Function

;ADD CRIME TO RECORD
Function AddCrime(crime)
 For count=1 To 20
  If crime<>0 And gamSins(slot,count)=0 Then gamSins(slot,count)=crime : crime=0 
 Next
End Function

;FIND LATEST CRIME
Function FindCrime()
 gamCrime(slot)=0
 For count=1 To 20
  If gamSins(slot,count)>gamSins(slot,gamCrime(slot)) Then gamCrime(slot)=count
 Next
End Function

;ASSIGN NEW MISSION
Function AssignMission()
 Repeat
  gamMission(slot)=Rnd(1,no_missions)
  If gamMission(slot)=14 Or gamMission(slot)=15
   If CountMissions()=<5 Then gamMission(slot)=0 ;delay slaying missions
  EndIf
 Until gamMission(slot)>0 And gamResult(slot,gamMission(slot))=0
End Function

;COUNT CRIMES (committed)
Function CountCrimes()
 value=0
 For count=1 To 20
  If gamSins(slot,count)<>0 Then value=value+1
 Next
 Return value
End Function

;COUNT MISSIONS (completed)
Function CountMissions()
 value=0
 For count=1 To no_missions
  If gamResult(slot,count)=1 Then value=value+1
 Next
 Return value
End Function

;MISSIONS REMAINING
Function MissionsLeft()
 value=0
 For count=1 To no_missions
  If gamResult(slot,count)=0 Then value=value+1
 Next
 Return value
End Function

;EMPTY HANDS UPON DELIVERY
Function EmptyHands(cyc,item)
 If item=0 Or iType(pCarryA(cyc))=item 
  v=pCarryA(cyc) : pCarryA(cyc)=0
  iX#(v)=Rnd(-460,-405) : iZ#(v)=Rnd(-615,-590) : iY#(v)=130
  PositionEntity i(v),iX#(v),iY#(v),iZ#(v)
  iGravity#(v)=0 : iBouncer#(v)=Rnd(1.5,2.5)
  iState(v)=2 : ShowEntity i(v)
 EndIf
 If item=0 Or iType(pCarryB(cyc))=item
  v=pCarryB(cyc) : pCarryB(cyc)=0
  iX#(v)=Rnd(-460,-405) : iZ#(v)=Rnd(-610,-590) : iY#(v)=130
  PositionEntity i(v),iX#(v),iY#(v),iZ#(v)
  iGravity#(v)=0 : iBouncer#(v)=Rnd(1.5,2.5)
  iState(v)=2 : ShowEntity i(v)
 EndIf 
 pAnimTim(cyc)=0
End Function

;COUNT ANIMALS
Function CountAnimals(animal)
 value=0
 For count=1 To no_plays
  If (pType(count)=animal Or animal=0) And pState(count)>0 
   gamSurvivor=count : value=value+1
  EndIf
 Next
 Return value
End Function