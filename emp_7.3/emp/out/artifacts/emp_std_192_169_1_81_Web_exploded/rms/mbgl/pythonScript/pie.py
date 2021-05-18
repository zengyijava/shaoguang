#!/usr/bin/env python
# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import numpy as np
from pylab import *
import os
mpl.rcParams['font.sans-serif'] = ['SimHei']

# make a square figure and axes
figure(1, figsize=(4,4))

ax =plt.subplot(111)

font_size = 10
mpl.rcParams['font.size'] = font_size
#ax = axes([0.1, 0.1, 0.8, 0.8])
labels = tuple(eval(sys.argv[1]))
fracs = list(tuple(eval(sys.argv[2])))
colors = list(tuple(eval(sys.argv[3])))
time1 = sys.argv[5]
time2 = sys.argv[6]
url = sys.argv[6]+'/'+sys.argv[5]+'.png'

#fracs =  [int(sys.argv[2]),int(sys.argv[3]),int(sys.argv[4]),int(sys.argv[5])]
#labels = '中国','哈哈','Dogs','Logs'
#colors = ['red','yellowgreen','lightskyblue','green']
#print (labels,type(labels))
#print (fracs,type(fracs))
#print (colors,type(colors))
#plt.legend(prop=zhfont1)
#explode=(0, 0.05, 0, 0)
#pie(fracs, explode=explode, labels=labels, autopct='%1.1f%%', shadow=True)
pie(fracs,colors=colors, autopct='%.0f%%',shadow=False,startangle=90,labeldistance =1)
#title('Raining Hogs and Dogs')

n = len(labels)

#a:标题相对Y轴的偏移量；b:图片相对x轴的偏移量；c:图片相对y轴的偏移量；d:图片长宽相对画布的比例；e:图例相对Y轴的偏移量
if n>8 :
	a = 0.95
	b = 0.15
	c = 0.25
	d = 0.9
	e = -(22+n)*0.01
	
else :
	a = 0.98
	b = 0.11
	c = 0.15
	d = 1
	e = -(10+n)*0.01

title(sys.argv[4],fontsize=12,x=0.5,y=a)

box=ax.get_position()
ax.set_position([b,c,box.width*d,box.height*d])
ax.legend(loc='lower center',bbox_to_anchor=(0.5,e),ncol=2,fontsize=8, labels=labels)

#legend(loc='upper left', bbox_to_anchor=(-0.1, 1))
grid()
#rcParams['axes.unicode_minus']=False
savefig(url)

#show()