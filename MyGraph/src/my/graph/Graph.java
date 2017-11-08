package my.graph;

import java.util.ArrayList;
import java.util.Iterator;

public class Graph {

	VNode[] vList;
	int vNum;
	int eNum;
	int cNum;

	int[] vex;
	int[] color;
	int[][] edges;

	// edges是个二维数组，{（v1，v2,color）}的形式
	public void init(int[] vex, int[][] edges) {
		this.vNum = vex.length;
		this.vList = new VNode[vNum];
		this.vex = vex;
		this.edges = edges;

		// 初始化顶点数组
		for (int i = 0; i < vNum; ++i) {
			vList[i] = new VNode();
			vList[i].data = vex[i];
		}

		// 存储边信息
		if (edges != null) {
			for (int[] c : edges) {
				int color = c[2];

				addEdges(c[0], c[1], color);

			}

		} else {
//			color = new int[0];
//			edges = new int[0][0];
		}
	}

	private int getPosition(int c) {
		for (int i = 0; i < this.vNum; ++i) {
			if (vList[i].data == c) {
				return i;
			}
		}
		return -1;
	}

	// 向图中添加一条边
	public void addEdges(int vex1, int vex2, int color) {
		int p1 = getPosition(vex1);
		int p2 = getPosition(vex2);
		vList[p1].add(p2, color);
		vList[p2].add(p1, color);
//		 更新边的信息
		if(this.edges==null){
			this.edges=new int[0][0];
		}
		int[][] tempedges = new int[edges.length + 1][3];
		for (int i = 0; i < edges.length; ++i) {
			tempedges[i] = edges[i];
		}
		tempedges[tempedges.length - 1][0] = vex1;
		tempedges[tempedges.length - 1][1] = vex2;
		tempedges[tempedges.length - 1][2] = color;
		this.edges = tempedges;

		// 更新边的个数
		this.eNum++;
//		 更新颜色数组
		boolean flag = false;
		if (this.color == null) {
			flag = false;
			this.color=new int[0];
		} else {
			for (int i = 0; i < this.color.length; ++i) {
				if (color == this.color[i]) {
					flag = true;
					break;
				}
			}
		}

		if (!flag) {
			int[] tempcolor = new int[this.color.length + 1];
			for (int i = 0; i < this.color.length; ++i) {
				tempcolor[i] = this.color[i];
			}
			tempcolor[tempcolor.length - 1] = color;
			this.cNum++;
			this.color=tempcolor;
		}
	}

	public void removeEfges(int vex1, int vex2) {
		vList[vex1].remove(vex2);
		vList[vex2].remove(vex1);
	}

	public boolean removeEdge(int c, int d) {
		int vex1 = getPosition(c);
		int vex2 = getPosition(d);
		// System.out.println(vex1+"dgdfbg"+vex2);
		return vList[vex1].remove(vex2) && vList[vex2].remove(vex1);

	}

}
