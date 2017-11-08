package my.graph;

import java.util.Random;

public class GenerateInstance {
	public Graph generateGraph(int vNum, int eNum, int cNum) {
		// �������һ��ͼ
		// �����������ȥ�ģ��ڵ�Ϊchar���Ϳ��������˰����øĳ�int�����ˣ���ɫ��int���͵ģ�Ҳ���������ȥ�ģ�����������ɫ�ĸ���
		int[] vex = new int[vNum];
		Random random = new Random();
		for (int i = 0; i < vNum; ++i) {
			vex[i] = i;
		}
		Graph g = new Graph();
		g.init(vex, null);
//		try {
			if (eNum > (vNum * (vNum - 1)) / 2) {
				throw new RuntimeException("��������");
			}
		
		for (int i = 0; i < eNum; ++i) {
			// ���ѡ�������ڵ����ɱ�
			int v1 = random.nextInt(vNum);
			int v2 = random.nextInt(vNum);
			while (v2 == v1) {
				v2 = random.nextInt(vNum);
			}
			// ��������ߴ����ˣ�Ҫ������ѡ��һ����
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
