states 11
initial
checkFirst
checkEnd
removeEndX
removeEndY
goToEndX
goToStartX
goToEndY
goToStartY
a +
r - 
alphabet 4 X Y s -
initial X goToEndX s R
initial Y goToEndY s R 
initial _ a _ R
removeEndX - removeEndX - L
removeEndX X goToStartX - L
removeEndX Y r - L
removeEndX s a s L
removeEndY - removeEndY - L
removeEndY X r - L
removeEndY Y goToStartY - L
removeEndY s a s L
checkFirst X goToEndX - R
checkFirst Y goToEndY - R
checkFirst _ a _ R
checkFirst - checkFirst - R
goToStartX - goToStartX - L
goToStartX X goToStartX X L
goToStartX Y goToStartX Y L
goToStartX s checkFirst s R 
goToStartY - goToStartY - L
goToStartY X goToStartY X L
goToStartY Y goToStartY Y L
goToStartY s checkFirst s R 
goToEndX - removeEndX - L
goToEndX X goToEndX X R
goToEndX Y goToEndX Y R
goToEndX _ removeEndX _ L
goToEndY - removeEndY - L
goToEndY X goToEndY X R
goToEndY Y goToEndY Y R
goToEndY _ removeEndY _ L
