package my.graph;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Alg {
	public static int mmcp(Graph g) {
		int[] bestS = generateInitialSolution(g);// ���ɳ�ʼ��
		int maxNeighborhood = g.cNum - bestS.length;
		boolean stopCondition = false;

		// ����bests
		int[] s = newSolution(g, bestS);
		while (true) {
			if (stopCondition) {
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
			int[] s1 = s;
			while (k < maxNeighborhood) {
				s1 = s;
				s1 = shake(s1, k, g);
				if (numberOfComponent(s1, g) == 1) {
					s1 = fix(s1, g);
				}
				s1 = localSearch(s1, g);
				if (s1.length > s.length) {
					s = s1;
					k = 1;
				} else {
					++k;
				}
			}
			if (s.length > bestS.length) {
				bestS = s;
				maxNeighborhood = g.cNum - bestS.length;
			}
		}
		return 0;

	}

	private static int[] localSearch(int[] s1, Graph g) {
		// TODO Auto-generated method stub
		return null;
	}

	private static int[] fix(int[] s2, Graph g) {
		// TODO Auto-generated method stub
		return null;
	}

	private static int numberOfComponent(int[] s2, Graph g) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int[] shake(int[] s2, int k, Graph g) {
		 
		return null;
	}

	private static int[] newSolution(Graph g, int[] bestS) {
		// TODO Auto-generated method stub

		// S�Ǹ���ʼ��ɫ����Ϊ��
		// ��bests�Ĳ�����s�Ĳ�����ѡ��(best*\s)��ɫ����(ÿ��Ҫ����ͨ��֧�����Ǹ������������¼����ӶȻ��д������������Ѷ���)��ֱ����ѡ��ɫ��Ϊ��ͨͼΪֹ
		// ��bests\s��ѡ����ɫ��ֱ������ѡΪֹ
		// ���������bests��best*
		ArrayList<Integer> sList = new ArrayList<>();
		HashMap<Integer, Integer> componentsNum = new HashMap<>();
		int[][] maxComponent = { { -1, 0 } };
		Graph spanGraph = new Graph();
		char[] vex = g.vex;
		char[][] edges = g.edges;
		int[] color = g.color;
		spanGraph.init(vex, null);// ��ʼ��ͼֻ�����ڵ�

		// ����ɫ��list����������Ϊ������Ҫɾ����ɫ��ȥ
		// ̰��
		ArrayList<Integer> colorlist = new ArrayList<>();
		// ���colorlistֻ��������bests*�е���ɫ
		for (int i = 0; i < color.length; ++i) {
			if (!isContainsColor(bestS, color[i])) {
				colorlist.add(color[i]);
			}
		}

		// ͳ�������ɫ��ȥ����ͨ��֧�ĸ���
		while (!colorlist.isEmpty()) {

			for (int k = 0; k < colorlist.size(); ++k) {
				// �ѵ�ǰʣ�µ���ɫ�ӽ���ͼ�����ͨ��֧������������ѡһ����ͨ��֧���������Ǹ�
				int tempcolor = colorlist.get(k);
				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == tempcolor)
						spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
				}
				int temp = dfs(spanGraph);
				// �洢��ͨ��֧��������Ǹ���ɫ�Լ���ͨ��֧����
				if (maxComponent[0][1] < temp) {
					maxComponent[0][0] = tempcolor;
					maxComponent[0][1] = temp;
				}
				componentsNum.put(tempcolor, temp);

				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == tempcolor)
						spanGraph.removeEdge(edges[i][0], edges[i][1]);
				}
			}
			// ��������ͨ��֧��������1�ˣ�����ӽ�ȥ
			if (maxComponent[0][1] > 1) {
				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == maxComponent[0][0])
						spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
				}
				// ������ɫ����ɫ�б���ɾ��
				colorlist.remove(Integer.valueOf(maxComponent[0][0]));
				// ��ӽ���ǰ�Ľ�s��ȥ
				sList.add(maxComponent[0][0]);
			} else {
				// ����Ļ����ͽ���ѭ����ִ����һ�ε�ѭ��
				break;
			}
		}

		// ������Ҫ��colorlist�����list������bests�е�����s�е���ɫ
		// TODO
		colorlist = new ArrayList<>();
		for (int i = 0; i < bestS.length; ++i) {
			if (!isContainsColor(sList, bestS[i])) {
				colorlist.add(bestS[i]);
			}
		}

		while (!colorlist.isEmpty()) {

			for (int k = 0; k < colorlist.size(); ++k) {
				// �ѵ�ǰʣ�µ���ɫ�ӽ���ͼ�����ͨ��֧������������ѡһ����ͨ��֧���������Ǹ�
				int tempcolor = colorlist.get(k);
				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == tempcolor)
						spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
				}
				int temp = dfs(spanGraph);
				// �洢��ͨ��֧��������Ǹ���ɫ�Լ���ͨ��֧����
				if (maxComponent[0][1] < temp) {
					maxComponent[0][0] = tempcolor;
					maxComponent[0][1] = temp;
				}
				componentsNum.put(tempcolor, temp);

				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == tempcolor)
						spanGraph.removeEdge(edges[i][0], edges[i][1]);
				}
			}
			// ��������ͨ��֧��������1�ˣ�����ӽ�ȥ
			if (maxComponent[0][1] > 1) {
				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == maxComponent[0][0])
						spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
				}
				// ������ɫ����ɫ�б���ɾ��
				colorlist.remove(Integer.valueOf(maxComponent[0][0]));
				// ��ӽ���ǰ�Ľ�s��ȥ
				sList.add(maxComponent[0][0]);
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

	private static boolean isContainsColor(ArrayList<Integer> s, int c) {
		int len = s.size();
		for (int i = 0; i < len; ++i) {
			if (s.get(i) == c) {
				return true;
			}
		}
		return false;
	}

	private static boolean isContainsColor(int[] bestS, int c) {
		int len = bestS.length;
		for (int i = 0; i < len; ++i) {
			if (bestS[i] == c)
				return true;
		}
		return false;
	}

	private static int[] generateInitialSolution(Graph g) {
		// TODO test this function whether enable
		ArrayList<Integer> list = new ArrayList<>();
		while (dfs(g) > 1) {
			// ������ɫ����
			//
			// ���ݺ�����Щ��ɫ�ıߣ�ʹ��Щ�߹���һ����ͼ����ͼ
			// ������ͼ����ͨ��֧�ĸ���
			// ���ж�Ҫ�����ĸ���ɫ��
			// һ������洢�Ѿ�ѡ������ɫ��bests����һ������洢δѡ����ɫ��S\bests��
			//
			Graph spanGraph = new Graph();
			char[] vex = g.vex;
			char[][] edges = g.edges;
			int[] color = g.color;
			spanGraph.init(vex, null);// ��ʼ��ͼֻ�����ڵ�

			for (int i = 0; i < color.length; ++i) {
				// ��ͼ�к��и���ɫ�ı�ȫ����ӽ�ȥ�����һ���߾��ж�һ�Σ������ͨ�ˣ���continue��
				for (int j = 0; j < edges.length; ++j) {
					if (edges[j][2] == color[i])
						spanGraph.addEdges(edges[j][0], edges[j][1], edges[j][2]);
					if (dfs(spanGraph) == 1) {// �����Ӹñ߽�ȥ ͼ��Ϊ��ͨͼ�ˣ��Ƴ��ñߣ����Ҳ�����Ӹ���ɫ
						spanGraph.removeEdge(edges[j][0], edges[j][1]);
						continue;
					}
					list.add(color[i]);
				}
				// �ж���ӽ�ȥ�Ժ��ͼ�Ƿ��Ǹ���ͨͼ
				// ����ǣ�����ӣ���������ӽ�ȥ
			}
		}
		int[] bestS = new int[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			bestS[i] = list.get(i);
		}
		return bestS;
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
				System.out.println(g.vList[i].data);

				while (!stack.isEmpty()) {
					int vex = stack.get(stack.size() - 1);
					Edge e = g.vList[vex].firstEdge;
					while (e != null) {// һ������
						if (visited[e.vex] == 1)
							e = e.next;
						else {
							visited[e.vex] = 1;
							stack.add(e.vex);
							System.out.println(g.vList[e.vex].data);
							e = g.vList[e.vex].firstEdge;
						}
					}
					stack.remove(stack.size() - 1);
				}
			}
		}

		return count;

	}

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
