package my.graph;

import java.util.Random;

public class GenerateInstance {
	public Graph generateGraph(int vNum, int eNum, int cNum) {
		// 随机生成一个图
		// 边是随机加上去的，节点为char类型看样不行了啊，得改成int类型了，颜色是int类型的，也是随机加上去的，根据所给颜色的个数
		int[] vex = new int[vNum];
		Random random = new Random();
		for (int i = 0; i < vNum; ++i) {
			vex[i] = i;
		}
		Graph g = new Graph();
		g.init(vex, null);
//		try {
			if (eNum > (vNum * (vNum - 1)) / 2) {
				throw new RuntimeException("边数超了");
			}
		
		for (int i = 0; i < eNum; ++i) {
			// 随机选择两个节点生成边
			int v1 = random.nextInt(vNum);
			int v2 = random.nextInt(vNum);
			while (v2 == v1) {
				v2 = random.nextInt(vNum);
			}
			// 如果这条边存在了，要再重新选择一条边
			while (isContainEage(v1, v2, g)) {
				v1 = random.nextInt(vNum);
				v2 = random.nextInt(vNum);
				while (v2 == v1) {
					v2 = random.nextInt(vNum);
				}
			}
			int color=random.nextInt(cNum);
			g.addEdges(v1, v2, color);
		}
		return g;

	}

	private boolean isContainEage(int v1, int v2, Graph g) {
		if(g.edges==null){
			return false;
		}
		int[][] edges = g.edges;
		for (int i = 0; i < edges.length; ++i) {
			if ((edges[i][0] == v1 && edges[i][1] == v2) || (edges[i][0] == v2 && edges[i][1] == v1)) {
				return true;
			}
		}
		return false;
	}

}
