#!/usr/bin/env python
# -*- coding: utf-8 -*-  
import matplotlib.pyplot as plt
import numpy as np
from pylab import *
mpl.rcParams['font.sans-serif'] = ['SimHei'] 
#对应画布的大小
figure(1, figsize=(12,10))
ax =plt.subplot(111)

#print (sys.argv[1],sys.argv[2],sys.argv[3])

#x轴的名称
#name_list = ['Monday','Tuesday','Friday','Sunday']  
name = sys.argv[1]
if ',' in name :
	name_list = list(tuple(eval(sys.argv[1]))) 
else :
	name_list = [name]


# 数据列表，多维数组
#num_list=[[1.5,0.6,7.8,6],[1,2,3,1],[2,3,2,3]]
num = sys.argv[2]
if '],' in num :
	num_list=list(tuple(eval(sys.argv[2])))
else :
	numList = list(tuple(eval(num)))
	numlist = [int(i) for i in numList]
	num_list = [numlist]

#数据分类LABLE名称	
#lable_list=['boy','girl','ohter']

label = sys.argv[3]
if ',' in label :
	lable_list=list(tuple(eval(sys.argv[3])))
else :
	lable_list = [label]

#报表标题
b_title=sys.argv[4]

#print (num_list,type(num_list))
#x轴的个数数组
x =list(range(len(name_list)))
#总的宽度
total_width = 0.8 
#x轴的个数
n = len(name_list)
#y轴个数
m = len(num_list)
#每个柱状图柱子的宽度大小
if (n+m) < 6:
	width = total_width / 5
elif (n+m) <10:
	width = total_width / (n+m-2)
elif (n+m) <15:	
	width = total_width / (n+m-3) 
else :
 	width = total_width / (n+m-5)
#图片保存地址
url = sys.argv[6]+'/'+sys.argv[5]+'.png'

#colors = ['#ffdb6b','#ef7f6a','#bed432','#c0b643','#7ac043','#28d96d','#18e0d4','#27e3ff','#00caf5','#5fb2ff','#00b4ff','#3b7eff','#4c58ed','#6d5bf5','#ac70f9','#ef7f6a','#b34eb1','#ff6b95','#f5505f']
colors = ['#37a2da','#ff9f7e','#66e1e3','#ffdb5c','#fb7293','#97bfff','#e162af','#9fe7b9','#e791d1','#e7bdf3','#32c5e9','#9d97f5','#8378eb']
  
#根据数据循环遍历画出柱状图

plt.xticks(fontsize=22)
plt.yticks(fontsize=22)

#第一根柱子下显示X轴标签
#plt.bar(x, num_list[0], width=width, label=lable_list[0],tick_label = name_list,color = colors[0] )  

#获取柱子的个数，向上取整，用于Y轴坐标的定位
a = math.ceil(m/2)

#之后每个柱子相对于第一根柱子的宽度都得要加一点，且不显示数据分类的lable
for i in range(len(num_list)): 
	for j in range(len(x)): 
		x[j] = x[j] + width  
	if (i+1)==a :
		plt.bar(x, num_list[i], width=width, label=lable_list[i],color = colors[i],tick_label = name_list) 
	else :
		plt.bar(x, num_list[i], width=width, label=lable_list[i],color = colors[i])

plt.title(b_title,fontsize=30,x=0.5,y=1.05)

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
plt.close()
