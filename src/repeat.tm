states 12
initial
check
checkX
checkY
moveRight
goToEndX
goToEndY
lastCheckX
lastCheckY
finished
a +
r -
alphabet 7 A B X Y 1 0 -
initial _ a _ R 
initial X moveRight A R 
initial Y moveRight B R 
moveRight 1 moveRight X R 
moveRight 1 check 1 L
moveRight 0 moveRight Y R 
moveRight 0 check 0 L
moveRight _ r _ R 
check X goToEndX - R
check Y goToEndY - R 
check 1 check 1 L 
check 0 check 0 L 
check - check - L 
check A lastCheckX A R 
check B lastCheckY B R 
goToEndX - goToEndX - R
goToEndX 1 goToEndX 1 R
goToEndX 0 goToEndX 0 R
goToEndX _ checkX _ L
goToEndY - goToEndY - R
goToEndY 1 goToEndY 1 R
goToEndY 0 goToEndY 0 R
goToEndY _ checkY _ L
lastCheckX - lastCheckX - R 
lastCheckX 1 finished - R
lastCheckY - lastCheckY - R 
lastCheckY 0 finished - R 
finished - finished - R 
finished _ a _ R 
