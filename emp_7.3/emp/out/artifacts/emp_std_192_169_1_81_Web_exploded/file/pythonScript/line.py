#!/usr/bin/env python
# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import numpy as np
from pylab import *
mpl.rcParams['font.sans-serif'] = ['SimHei']

# make a square figure and axes
figure(1, figsize=(12,10))
ax =plt.subplot(111)
#x=range(0,10)
name = sys.argv[1]
if ',' in name :
	x=list(tuple(eval(sys.argv[1])))
else :
	x = [name]


#y_list = [[10,13,5,40,30,60,70,12,55,25],[5,8,0,30,20,40,50,10,40,15],[7,18,20,0,20,40,50,30,20,15]]

num = sys.argv[2]
if '],' in num :
	y_list = list(tuple(eval(sys.argv[2])))
else :
	numList = list(tuple(eval(num)))
	numlist = [int(i) for i in numList]
	y_list = [numlist]

labels = sys.argv[3]
if ',' in labels :
	label = list(tuple(eval(sys.argv[3])))
else :
	label = [labels]

y =list(range(len(y_list)))
tit = sys.argv[4]

#colors = ['#ffdb6b','#ef7f6a','#bed432','#c0b643','#7ac043','#28d96d','#18e0d4','#27e3ff','#00caf5','#5fb2ff','#00b4ff','#3b7eff','#4c58ed','#6d5bf5','#ac70f9','#ef7f6a','#b34eb1','#ff6b95','#f5505f']
colors = ['#37a2da','#ff9f7e','#66e1e3','#ffdb5c','#fb7293','#97bfff','#e162af','#9fe7b9','#e791d1','#e7bdf3','#32c5e9','#9d97f5','#8378eb']

for i in range(len(y_list)):
	plt.plot(range(0,len(y_list[i])),y_list[i],label=label[i],color=colors[i],lw=3)
	#plt.plot(x,y_list[i],label=label[i])
plt.xticks(range(0,len(y_list[0])), x, rotation=0)
#图片保存地址
url = sys.argv[6]+'/'+sys.argv[5]+'.png'
plt.title(tit,fontsize=30,x=0.5,y=1.05) 

plt.xticks(fontsize=22)
plt.yticks(fontsize=22)

m=len(y)
if m<6:
	b = -0.28
elif m<9:
	b = -0.35
else :
	b = -0.4

box=ax.get_position()
ax.set_position([0.11,0.25,box.width,box.height*0.8])
ax.legend(loc='lower center',bbox_to_anchor=(0.5,b),fontsize=18,ncol=3)

savefig(url) 