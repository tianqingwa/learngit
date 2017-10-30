package my.graph;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Alg {
	public static int mmcp(Graph g) {
		int[] bestS = generateInitialSolution(g);// 生成初始解
		int maxNeighborhood = g.cNum - bestS.length;
		boolean stopCondition = false;

		// 改善bests
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

			// vns变邻域搜索
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

		// S是个初始颜色集合为空
		// 从bests的补集的s的补集中选择(best*\s)颜色进来(每次要加联通分支最多的那个进来啊！这事件复杂度还有处理起来可困难多了)，直到再选颜色变为连通图为止
		// 从bests\s中选择颜色，直到不能选为止
		// 互相掺和了bests和best*
		ArrayList<Integer> sList = new ArrayList<>();
		HashMap<Integer, Integer> componentsNum = new HashMap<>();
		int[][] maxComponent = { { -1, 0 } };
		Graph spanGraph = new Graph();
		char[] vex = g.vex;
		char[][] edges = g.edges;
		int[] color = g.color;
		spanGraph.init(vex, null);// 初始子图只包含节点

		// 把颜色用list存起来，因为接下来要删除颜色出去
		// 贪心
		ArrayList<Integer> colorlist = new ArrayList<>();
		// 这个colorlist只包含，在bests*中的颜色
		for (int i = 0; i < color.length; ++i) {
			if (!isContainsColor(bestS, color[i])) {
				colorlist.add(color[i]);
			}
		}

		// 统计添加颜色进去后连通分支的个数
		while (!colorlist.isEmpty()) {

			for (int k = 0; k < colorlist.size(); ++k) {
				// 把当前剩下的颜色加进子图后的连通分支个数存起来，选一个联通分支个数最大的那个
				int tempcolor = colorlist.get(k);
				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == tempcolor)
						spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
				}
				int temp = dfs(spanGraph);
				// 存储连通分支个数醉的那个颜色以及连通分支个数
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
			// 如果最大连通分支个数大于1了，就添加进去
			if (maxComponent[0][1] > 1) {
				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == maxComponent[0][0])
						spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
				}
				// 将该颜色从颜色列表中删除
				colorlist.remove(Integer.valueOf(maxComponent[0][0]));
				// 添加进当前的解s中去
				sList.add(maxComponent[0][0]);
			} else {
				// 否则的话，就结束循环，执行下一次的循环
				break;
			}
		}

		// 这里需要求colorlist，这个list包含在bests中但不在s中的颜色
		// TODO
		colorlist = new ArrayList<>();
		for (int i = 0; i < bestS.length; ++i) {
			if (!isContainsColor(sList, bestS[i])) {
				colorlist.add(bestS[i]);
			}
		}

		while (!colorlist.isEmpty()) {

			for (int k = 0; k < colorlist.size(); ++k) {
				// 把当前剩下的颜色加进子图后的连通分支个数存起来，选一个联通分支个数最大的那个
				int tempcolor = colorlist.get(k);
				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == tempcolor)
						spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
				}
				int temp = dfs(spanGraph);
				// 存储连通分支个数醉的那个颜色以及连通分支个数
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
			// 如果最大连通分支个数大于1了，就添加进去
			if (maxComponent[0][1] > 1) {
				for (int i = 0; i < edges.length; ++i) {
					if (edges[i][2] == maxComponent[0][0])
						spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
				}
				// 将该颜色从颜色列表中删除
				colorlist.remove(Integer.valueOf(maxComponent[0][0]));
				// 添加进当前的解s中去
				sList.add(maxComponent[0][0]);
			} else {
				// 否则的话，就结束循环，执行下一次的循环
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
			// 遍历颜色集合
			//
			// 根据含有这些颜色的边，使这些边构成一个子图，子图
			// 根据子图的连通分支的个数
			// 来判断要纳入哪个颜色。
			// 一个数组存储已经选过的颜色（bests），一个数组存储未选的颜色（S\bests）
			//
			Graph spanGraph = new Graph();
			char[] vex = g.vex;
			char[][] edges = g.edges;
			int[] color = g.color;
			spanGraph.init(vex, null);// 初始子图只包含节点

			for (int i = 0; i < color.length; ++i) {
				// 将图中含有该颜色的边全部添加进去（添加一条边就判断一次，如果联通了，则continue）
				for (int j = 0; j < edges.length; ++j) {
					if (edges[j][2] == color[i])
						spanGraph.addEdges(edges[j][0], edges[j][1], edges[j][2]);
					if (dfs(spanGraph) == 1) {// 如果添加该边进去 图成为连通图了，移除该边，并且不能添加该颜色
						spanGraph.removeEdge(edges[j][0], edges[j][1]);
						continue;
					}
					list.add(color[i]);
				}
				// 判断添加进去以后的图是否是个连通图
				// 如果是，则不添加，不是则添加进去
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
		int[] visited = new int[g.vNum];// 定义一个标志数组
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
					while (e != null) {// 一次深搜
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
