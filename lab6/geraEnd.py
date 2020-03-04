arq = open('end.txt', 'w')

for i in range(0x700, 0x900, 2):
	print(format(i, '#06x').upper()[2:])



