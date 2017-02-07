package jp.ne.naoya.hori.codeiq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class ComboCounter {

	// 値をスキップできる最大数
	private static final int SKIP_MAX = 2;

	// 正の整数値の数が入力される行
	private static final int LINE_OF_MAX = 1;

	// デバッグモード
	// trueの時、debugログを出力します
	private static final boolean DEBUG = true;

	// 正常終了時のリターンコード
	private static final int STATUS_OK = 0;

	// 異常終了時のリターンコード
	private static final int STATUS_NG = 1;

	public static void main(String[] args) {
		try {
			// 求めたコンボのリストのうち、もっともサイズが大きいリストを取得し、そのサイズを標準出力に出力します。
			System.out.println(new ComboCounter().countCombo().stream().max((l1, l2) -> l1.size() - l2.size())
					.orElse(Collections.emptyList()).size());
			System.exit(STATUS_OK);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(STATUS_NG);
		}
	}

	/**
	 *
	 * 標準入力に入力された文字列からコンボを求めます。
	 *
	 * @return コンボの配列
	 * @throws Exception
	 *             標準入力からデータ読み込みで例外発生した場合
	 */
	public List<List<Integer>> countCombo() throws Exception {
		List<List<Integer>> list = new ArrayList<List<Integer>>();

		try (Scanner cin = new Scanner(System.in)) {
			String line;
			int numberOfline = 0;
			int max = 0;

			for (; cin.hasNext();) {
				line = cin.nextLine();

				// 正の整数値が入力された場合
				if (++numberOfline == LINE_OF_MAX) {
					max = Integer.parseInt(line);
				}
				// コンボ対象の数字が入力された場合
				else {
					List<Integer> integerList = stringToIntList(line);

					for (int i = 0; i < integerList.size() - 1; i++) {
						list.add(countComboFromList(integerList.subList(i, integerList.size()), max));
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * String から List<Integer> に変換するメソッドです。
	 *
	 * @param str
	 *            文字列
	 * @return List<Integer>型
	 */
	private List<Integer> stringToIntList(String str) {
		return Arrays.asList(
				Arrays.stream(str.split(" ")).mapToInt(Integer::parseInt).mapToObj(i -> i).toArray(Integer[]::new));
	}

	/**
	 * List<Integer>からコンボとなるIntegerのリストを返します。
	 *
	 * @param list
	 *            計算対象の整数のリスト
	 * @param max
	 *            整数の最大値
	 * @return コンボが成立したIntegerのリスト
	 */
	private List<Integer> countComboFromList(List<Integer> list, int max) {

		debug("Input list = " + list + ", max = " + max);

		List<Integer> returnList = new ArrayList<Integer>();
		Integer first = list.get(0);
		returnList.add(first);

		// リスト内の最大値
		int maxInList = first;

		// リスト内の最大値のインデックス
		int maxIndex = 0;

		for (int i = 1; i < list.size(); i++) {

			List<Integer> subList = list.subList(i, Integer.min(list.size(), i + SKIP_MAX + 1));
			int target = getMinBetweenRange(subList, maxInList);
			int targetIndex = i + subList.indexOf(target);

			debug("target =" + target + ", targetIndex =  " + list.indexOf(target) + ", max = " + maxInList
					+ ",maxIndex = " + maxIndex + ", list.get(i) = " + list.get(i) + ", returnList = " + returnList);

			// 最大値を超える場合、又は、スキップ可能数を超えた場合はスキップ
			if ((target > max) || (targetIndex > maxIndex + SKIP_MAX + 1)) {
				debug("skiped...[target=" + target + ",max=" + max + ",targetIndex=" + targetIndex + ", maxIndex="
						+ maxIndex);
				continue;
			}

			if (target > maxInList) {

				if (!returnList.contains((Integer) target) && (targetIndex > maxIndex)) {
					returnList.add(target);
					i = targetIndex;
					debug("add to returnList : " + target);
				} else {
					debug("target = " + list.indexOf(target) + ", max = " + list.indexOf(maxInList));
				}
				maxInList = target;
				maxIndex = targetIndex;
			}

		}

		debug("result = " + returnList + ", size = " + returnList.size() + " ********* ");
		return returnList;
	}

	/**
	 * listのIntegerのうち、max未満、かつ、最小のIntegerを返します。
	 *
	 * @param list 対象のリスト
	 * @param max 整数の最大値
	 * @return max未満、かつ、最小のInteger
	 */
	private int getMinBetweenRange(List<Integer> list, int max) {
		debug("list = " + list.toString() + ", max = " + max);
		return list.stream().filter(i -> i > max).min(Comparator.naturalOrder()).orElse(-1);
	}

	/**
	 * デバッグ用のメソッドです。 DEBUG が true の時だけ、ログを出力します。
	 *
	 * @param str
	 *            デバッグメッセージ
	 */
	private void debug(String str) {
		if (DEBUG) {
			System.out.println("[DEBUG]" + str);
		}
	}

}
