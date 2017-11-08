package my.graph;

import java.util.ArrayList;
import java.util.Random;

public class Alg {
	public static int mccp(Graph g) {
		int[] bestS = generateInitialSolution(g);// ���ɳ�ʼ��
		int maxNeighborhood = g.cNum - bestS.length;
		int stopCondition = 0;

		// ����bests
		int[] s = newSolution(g, bestS);
		// ѭ����ʼ
		while (true) {
			if (stopCondition > 5) {
				break;
			}
			s = newSolution(g, bestS);
			while (s.length > bestS.length) {
				bestS = s;
				maxNeighborhood = g.cNum - bestS.length;
				s = newSolution(g, bestS);
			}

			// vns����������
			int k = 1;
			// ����ĳ�ѭ��������s����s1
			int[] s1 = new int[s.length];
			for (int i = 0; i < s.length; ++i) {
				s1[i] = s[i];
			}
			while (k < maxNeighborhood) {
				s1 = new int[s.length];
				for (int i = 0; i < s.length; ++i) {
					s1[i] = s[i];
				}
				s1 = shake(s1, k, g);
				if (numberOfComponent(s1, g) == 1) {
					s1 = fix(s1, g);
				}
//				System.out.println("s1�ĳ���:"+s1.length);
				s1 = localSearch(s1, g);
				if (s1.length > s.length) {
					s = new int[s1.length];
					for (int i = 0; i < s1.length; ++i) {
						s[i] = s1[i];
					}
					k = 1;
//					k++;
				} else {
					++k;
				}
			}
			if (s.length > bestS.length) {
				bestS = new int[s.length];
				for (int i = 0; i < bestS.length; ++i) {
					bestS[i] = s[i];
				}

				maxNeighborhood = g.cNum - bestS.length;
			}
			stopCondition++;
		}
		return g.cNum - bestS.length;

	}

	public static int numberOfComponent(int[] s1, Graph g) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < s1.length; ++i) {
			list.add(s1[i]);
		}
		int[][] edges = g.edges;
		Graph spanGraph = new Graph();
		spanGraph.init(g.vex, null);
		for (int i = 0; i < list.size(); ++i) {
			for (int j = 0; j < edges.length; ++j) {

				if (edges[j][2] == list.get(i))// ������s2[i]��ɫ�ı���ӽ�ͼ��
					spanGraph.addEdges(edges[j][0], edges[j][1], edges[j][2]);
			}
		}
		return dfs(spanGraph);// ��������ͼ����ͨ��֧��
	}

	public static int[] localSearch(int[] s1, Graph g) {
		// TODO Auto-generated method stub
		// ���s'���ɵ���ͼ��ͨ��֧��������1
		// ��newsolutionһ����s'�Ĳ����������ɫ����̰�ĵ��㷨��
		// ��newsolution��
		ArrayList<Integer> curColor = new ArrayList<>();// ��ǰ��ɫ�ļ���
		ArrayList<Integer> remainColor = new ArrayList<>();// ʣ����ɫ�ļ���
		for (int i = 0; i < s1.length; ++i) {
			curColor.add(s1[i]);
		}
		for (int i = 0; i < g.color.length; ++i) {
			if (!curColor.contains(g.color[i])) {
				remainColor.add(g.color[i]);
			}
		}
		while (numberOfComponent(curColor, g) > 1) {
			int[] maxComponent = maxNumOfComponent(g, remainColor, curColor);
			if (maxComponent[1] > 1) {
				curColor.add(maxComponent[0]);
				remainColor.remove(Integer.valueOf(maxComponent[0]));
			} else {
				break;
			}
		}
		s1 = new int[curColor.size()];
		for (int i = 0; i < curColor.size(); ++i) {
			s1[i] = curColor.get(i);
		}
		return s1;
	}

	public static int[] maxNumOfComponent(Graph g, ArrayList<Integer> remainColor) {
		// TODO Auto-generated method stub
		int[] maxComponent = { 0, -1 };// ��������ͨ��֧�����Լ��ӽ�������ɫ
		int[][] edges = g.edges;// ��ű�
		Graph spanGraph = new Graph();
		spanGraph.init(g.vex, edges);
		// �Ƴ�������ʣ����ɫ�ı�
		for (int i = 0; i < g.color.length; ++i) {
			if (remainColor.contains(g.color[i])) {
				for (int j = 0; j < edges.length; ++j) {
					if (edges[j][2] == g.color[i]) {
						spanGraph.removeEdge(edges[j][0], edges[j][1]);
					}
				}
			}
		}
		for (int k = 0; k < remainColor.size(); ++k) {
			// �ѵ�ǰʣ�µ���ɫ�ӽ���ͼ�����ͨ��֧������������ѡһ����ͨ��֧���������Ǹ�
			int tempcolor = remainColor.get(k);
			for (int i = 0; i < edges.length; ++i) {
				if (edges[i][2] == tempcolor)
					spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
			}
			int temp = dfs(spanGraph);
			// �洢��ͨ��֧���������Ǹ���ɫ�Լ���ͨ��֧����
			if (maxComponent[1] < temp) {
				maxComponent[0] = tempcolor;
				maxComponent[1] = temp;

			}
			for (int i = 0; i < edges.length; ++i) {
				if (edges[i][2] == tempcolor)
					spanGraph.removeEdge(edges[i][0], edges[i][1]);
			}
		}
		return maxComponent;
	}

	public static int[] fix(int[] s2, Graph g) {
		// TODO Auto-generated method stub
		// ��s2������Ƴ���ɫ��ֱ��s2����ͨ��֧��������1
		ArrayList<Integer> list = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < s2.length; ++i) {
			list.add(s2[i]);
		}
		while (numberOfComponent(list, g) <= 1) {
			int nextInt = random.nextInt(list.size());
			list.remove(nextInt);
		}
		s2 = new int[list.size()];
		for (int i = 0; i < s2.length; ++i) {
			s2[i] = list.get(i);
		}
		return s2;
	}

	public static int numberOfComponent(ArrayList<Integer> s2, Graph g) {
		int[][] edges = g.edges;
		Graph spanGraph = new Graph();
		spanGraph.init(g.vex, null);
		for (int i = 0; i < s2.size(); ++i) {
			for (int j = 0; j < edges.length; ++j) {

				if (edges[j][2] == s2.get(i))// ������s2[i]��ɫ�ı���ӽ�ͼ��
					spanGraph.addEdges(edges[j][0], edges[j][1], edges[j][2]);
			}
		}
		return dfs(spanGraph);// ��������ͼ����ͨ��֧��
	}

	public static int[] shake(int[] s2, int k, Graph g) {
		// ͨ����ǰ�������ӻ����Ƴ�k����ɫ������һ���µĽ�s��
		// Ϊ�˶�������ķ�Χ����������ֲ�����
		// �ڴ��µĽ�s�����Ƴ���ɫʱҪ��鵱ǰ���Ƿ�Ϊ��
		// �����������ʱ��S��S'�Ĳ�������k����ɫ������s��s'�Ĳ�����ȥ�����Ľ������õļ�������ɫ�ĸ�����k��
		ArrayList<Integer> remainColor = new ArrayList<>();// ʣ�����ɫ����
		ArrayList<Integer> newAddColor = new ArrayList<>();// ����ӽ�����ɫ
		ArrayList<Integer> enableColor = new ArrayList<>();// �����򼯺�����ӵ���ɫ
		for (int i = 0; i < s2.length; ++i) {
			remainColor.add(s2[i]);
			// tempS.add(s2[i]);
		}

		for (int i = 0; i < g.color.length; i++) {// ������s2�е���ɫ��ӽ�enable color��
			if (!remainColor.contains(g.color[i])) {
				enableColor.add(g.color[i]);
			}
		}

		for (int i = 0; i < k; ++i) {
			Random random = new Random();
			float x = random.nextFloat();// Returns the next pseudorandom,
											// uniformly
			// distributed float value between 0.0 and 1.0
			// from this random number generator's sequence.
			if (x < 0.5 && remainColor.size() > 0) {
				// ����Ĵ�S'���Ƴ�һ����ɫ,�����ɫ����������ӽ�ȥ����ɫ
				// �����Ƴ��������ɫ����s��s'�Ľ����У����Բ���һ��0��length-1���������������Ǽ����Ƴ��ڼ���//Ҫ����s'�ȴ浽һ��arraylist�У��ڴ�����
				int index = random.nextInt(remainColor.size());// �������ɾ�����±�
				remainColor.remove(index);// �Ƴ�����ɫ
			} else {
				// ��������һ����ɫ
				if (enableColor.size() > 0) {
					int index = random.nextInt(enableColor.size());
					newAddColor.add(enableColor.get(index));
					enableColor.remove(index);// �Ƴ�����ɫ
				}

			}
		}
		s2 = new int[remainColor.size() + newAddColor.size()];
		int i = 0;
		// ��shake�����ɫ��װ��s2������
		for (; i < remainColor.size(); ++i) {
			s2[i] = remainColor.get(i);
		}
		for (; i < remainColor.size() + newAddColor.size(); ++i) {
			s2[i] = newAddColor.get(i - remainColor.size());
		}
		return s2;
	}

	public static int[] newSolution(Graph g, int[] bestS) {

		// S�Ǹ���ʼ��ɫ����Ϊ��
		// ��bests�Ĳ�����s�Ĳ�����ѡ��(best*\s)��ɫ����(ÿ��Ҫ����ͨ��֧�����Ǹ������������¼����ӶȻ��д������������Ѷ���)��ֱ����ѡ��ɫ��Ϊ��ͨͼΪֹ
		// ��bests\s��ѡ����ɫ��ֱ������ѡΪֹ
		// ���������bests��best*
		ArrayList<Integer> sList = new ArrayList<>();
		int[] maxComponent = { -1, 0 };
		int[] color = g.color;

		// ����ɫ��list����������Ϊ������Ҫɾ����ɫ��ȥ
		// ̰��
		// ����������ӵ���ɫ��best*\s������ΪsΪ�գ�����best*\s����best*��
		ArrayList<Integer> colorlist = new ArrayList<>();
		ArrayList<Integer> bestsList = new ArrayList<>();
		for (int i = 0; i < bestS.length; ++i) {
			bestsList.add(bestS[i]);
		}
		for (int i = 0; i < color.length; ++i) {
			if (!bestsList.contains(color[i])) {
				colorlist.add(color[i]);
			}
		}

		// ͳ�������ɫ��ȥ����ͨ��֧�ĸ���
		while (!colorlist.isEmpty()) {
			maxComponent = maxNumOfComponent(g, colorlist, sList);
			// ��������ͨ��֧��������1�ˣ�����ӽ�ȥ
			if (maxComponent[1] > 1) {
				// ������ɫ����ɫ�б���ɾ��
				colorlist.remove(Integer.valueOf(maxComponent[0]));
				// ��ӽ���ǰ�Ľ�s��ȥ
				sList.add(maxComponent[0]);
			} else {
				// ����Ļ����ͽ���ѭ����ִ����һ�ε�ѭ��
				break;
			}
		}

		colorlist = bestsList;
		while (!colorlist.isEmpty()) {
			maxComponent = maxNumOfComponent(g, colorlist, sList);
			// ��������ͨ��֧��������1�ˣ�����ӽ�ȥ
			if (maxComponent[1] > 1) {
				// ������ɫ����ɫ�б���ɾ��
				colorlist.remove(Integer.valueOf(maxComponent[0]));
				// ��ӽ���ǰ�Ľ�s��ȥ
				sList.add(maxComponent[0]);
			} else {
				// ����Ļ����ͽ���ѭ����ִ����һ�ε�ѭ��
				break;
			}
		}

		int[] s = new int[sList.size()];
		for (int i = 0; i < s.length; ++i) {
			s[i] = sList.get(i);
		}

		return s;

	}

	private static int[] maxNumOfComponent(Graph g, ArrayList<Integer> remainColor, ArrayList<Integer> sList) {
		int[] maxComponent = { 0, -1 };// ��������ͨ��֧�����Լ��ӽ�������ɫ
		int[][] edges = g.edges;// ��ű�
		Graph spanGraph = new Graph();
		spanGraph.init(g.vex, null);
		//����Ѱ�������ɫ�ı�
		for (int i = 0; i < sList.size(); ++i) {
			for (int j = 0; j < edges.length; ++j) {
				if (edges[j][2] == sList.get(i)) {
					spanGraph.addEdges(edges[j][0], edges[j][1], edges[j][2]);
				}
			}
		}
		for (int k = 0; k < remainColor.size(); ++k) {
			// �ѵ�ǰʣ�µ���ɫ�ӽ���ͼ�����ͨ��֧������������ѡһ����ͨ��֧���������Ǹ�
			int tempcolor = remainColor.get(k);
			for (int i = 0; i < edges.length; ++i) {
				if (edges[i][2] == tempcolor)
					spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
			}
			int temp = dfs(spanGraph);
			// �洢��ͨ��֧���������Ǹ���ɫ�Լ���ͨ��֧����
			if (maxComponent[1] < temp) {
				maxComponent[0] = tempcolor;
				maxComponent[1] = temp;

			}
			for (int i = 0; i < edges.length; ++i) {
				if (edges[i][2] == tempcolor)
					spanGraph.removeEdge(edges[i][0], edges[i][1]);
			}
		}
		return maxComponent;
	}

	public static int[] generateInitialSolution(Graph g) {
		// TODO test this function whether enable
		ArrayList<Integer> bestsList = new ArrayList<>();//
		ArrayList<Integer> colorlist = new ArrayList<>();
		int[] color = g.color;
		int[] maxComponent = { 0, 1 };// {{��ɫ����ͨ��֧����}}
		for (int i = 0; i < color.length; ++i) {
			colorlist.add(color[i]);
		}
		while (!colorlist.isEmpty()) {
			maxComponent = maxNumOfComponent(g, colorlist, bestsList);
			// ��������ͨ��֧��������1�ˣ�����ӽ�ȥ
			// System.out.println("*****��ͨ��֧������" + maxComponent[1] + " ��ɫ:" +
			// maxComponent[0]);
			if (maxComponent[1] > 1) {
				// ������ɫ����ɫ�б���ɾ��
				colorlist.remove(Integer.valueOf(maxComponent[0]));
				// ��ӽ���ǰ�Ľ�s��ȥ
				bestsList.add(maxComponent[0]);
			} else {
				// ����Ļ����ͽ���ѭ����ִ����һ�ε�ѭ��
				break;
			}
		}
		int[] bests = new int[bestsList.size()];
		for (int i = 0; i < bestsList.size(); ++i) {
			bests[i] = bestsList.get(i);
		}
		return bests;
	}

	public static int dfs(Graph g) {
		int count = 0;
		int[] visited = new int[g.vNum];// ����һ����־����
		ArrayList<Integer> stack = new ArrayList<>();
		for (int i = 0; i < g.vNum; ++i) {
			visited[i] = 0;
		}

		for (int i = 0; i < g.vNum; ++i) {
			if (visited[i] == 0) {
				count++;
				visited[i] = 1;
				stack.add(i);
				// System.out.println(g.vList[i].data);

				while (!stack.isEmpty()) {
					int vex = stack.get(stack.size() - 1);
					Edge e = g.vList[vex].firstEdge;
					while (e != null) {// һ������
						if (visited[e.vex] == 1)
							e = e.next;
						else {
							visited[e.vex] = 1;
							stack.add(e.vex);
							// System.out.println(g.vList[e.vex].data);
							e = g.vList[e.vex].firstEdge;
						}
					}
					stack.remove(stack.size() - 1);
				}
			}
		}

		return count;

	}

	// �ݹ��������������
	public static void dfs(int[] visited, Graph g, int i) {

		if (visited[i] == 0) {
			visited[i] = 1;
			System.out.println(g.vList[i].data);
			Edge e = g.vList[i].firstEdge;
			while (e != null) {
				dfs(visited, g, e.vex);
				e = e.next;
			}
		}

	}

	// public static void bfs(int[] visited, Graph g, int i) {
	// if (visited[i] == 0) {
	// visited[i] = 1;
	// System.out.println(g.vList[i].data);
	// Edge e = g.vList[i].firstEdge;
	// while (e != null) {
	//
	// }
	// }
	// }

}
