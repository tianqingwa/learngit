package my.graph;

import java.util.ArrayList;
import java.util.Iterator;

public class Graph {

	VNode[] vList;
	int vNum;
	int eNum;
	int cNum;

	char[] vex;
	int[] color;
	char [][]edges;
	// edges是个二维数组，{（v1，v2,color）}的形式
	public void init(char[] vex, char[][] edges) {
		this.vNum = vex.length;
		this.vList = new VNode[vNum];
		this.vex = vex;
		this.edges=edges;
		
		ArrayList<Integer> temp = new ArrayList<>();// 辅助list，为了后面初始化颜色数组
		// 初始化顶点数组
		for (int i = 0; i < vNum; ++i) {
			vList[i] = new VNode();
			vList[i].data = vex[i];
		}

		// 存储边信息
		if (edges != null) {
			for (char[] c : edges) {
				int color = c[2];
				if (!temp.contains(color)) {
					temp.add(color);
				}
				int p1 = getPosition(c[0]);
				int p2 = getPosition(c[1]);

				addEdges(p1, p2, color);
			}
			// 初始化颜色数组
			this.color = new int[temp.size()];
			for (int i = 0; i < temp.size(); ++i) {
				color[i] = temp.get(i);
			}
		}
	}

	private int getPosition(char c) {
		for (int i = 0; i < this.vNum; ++i) {
			if (vList[i].data == c) {
				return i;
			}
		}
		return -1;
	}

	//向图中添加一条边
	public void addEdges(int vex1, int vex2, int color) {
		vList[vex1].add(vex2, color);
		vList[vex2].add(vex1, color);
	}
	public void removeEfges(int vex1,int vex2){
		vList[vex1].remove(vex2);
		vList[vex2].remove(vex1);
	}

	public void removeEdge(char c, char d) {
		int vex1=getPosition(c);
		int vex2=getPosition(c);
		vList[vex1].remove(vex2);
		vList[vex2].remove(vex1);
		
	}

}
