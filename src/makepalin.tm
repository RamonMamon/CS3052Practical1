states 11
initial
goToEnd
goBack
checkLast
copyX
copyY
lastCopyX
lastCopyY
revert
a +
r -
alphabet 6 1 0 x y X Y
initial X goToEnd 1 R 
initial Y goToEnd 0 R 
initial _ a _ R 
goToEnd X goToEnd X R 
goToEnd Y goToEnd Y R 
goToEnd x goToEnd x R 
goToEnd y goToEnd y R 
goToEnd _ checkLast _ L 
checkLast x checkLast x L 
checkLast y checkLast y L 
checkLast X copyX x R 
checkLast Y copyY y R
checkLast 1 lastCopyX 1 R 
checkLast 0 lastCopyY 0 R
goBack X goBack X L 
goBack Y goBack Y L 
goBack x checkLast x L 
goBack y checkLast y L 
lastCopyX x lastCopyX x R 
lastCopyX y lastCopyX y R 
lastCopyX X lastCopyX X R 
lastCopyX Y lastCopyX Y R 
lastCopyX _ revert X L
lastCopyY x lastCopyY x R 
lastCopyY y lastCopyY y R 
lastCopyY X lastCopyY X R 
lastCopyY Y lastCopyY Y R
lastCopyY _ revert Y L 
revert X revert X L 
revert Y revert Y L
revert x revert X L 
revert y revert Y L 
revert 0 a Y L 
revert 1 a X L
copyX X copyX X R
copyX Y copyX Y R 
copyX x copyX x R 
copyX y copyX y R 
copyX _ goBack X L 
copyY X copyY X R
copyY Y copyY Y R 
copyY x copyY x R 
copyY y copyY y R 
copyY _ goBack Y L