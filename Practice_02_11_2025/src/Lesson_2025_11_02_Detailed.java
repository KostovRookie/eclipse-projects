import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Урок (02.11.2025) – Java: масиви, foreach, обхождане, map/filter/reduce, split/regex,
 * 2D масиви, търсене/филтриране/индекс, формат на печат, Record-и и работа с Object[].
 *
 * Компилация/стартиране:
 *   javac Lesson_2025_11_02_Detailed.java
 *   java Lesson_2025_11_02_Detailed
 *
 * Съдържание:
 *   0) Теория - супер кратки бележки (в коментари)
 *   1) String обработка: replaceAll, trim, split, foreach (вдъхновено от Project_05_06_Strings)
 *   2) Масиви + Random + Arrays.setAll + streams + join + boxing (Project_06 идеи)
 *   3) Филтриране и търсене в long[] (итеративно и със streams) + indexOf (Project_03_12, Project_08)
 *   4) 2D масиви: подравнен печат, размяна на редове, суми/макс по ред/колона, transpose
 *   5) Object[] → record RT (стойност + тип), показва map-ване към метаданни (Project_03_14)
 *   6) Допълнителни мини-задачи: min/max, сортиране, distinct, честоти (HashMap), map/filter/reduce сравнение
 *
 * Всички демота се извикват от main() по ред и печатат резултати.
 *
 * ЧЕСТИ ГРЕШКИ:
 *  - ArrayIndexOutOfBoundsException: винаги пази границите 0..length-1
 *  - NullPointerException: валидирай null преди достъп
 *  - == за низове е грешно – ползвай s.equals("...") или Objects.equals(a,b)
 *  - Streams върху примитивни масиви: Arrays.stream(int[]) дава IntStream (без авто-box)
 */

/* ==========================
 * 0) ТЕОРИЯ (кратко напомняне)
 * ----------------------------
 * Масив: фиксирана дължина, хомогенен тип. Сложности:
 *  - достъп по индекс: O(1)
 *  - търсене линейно: O(n)
 *  - поставяне/премахване в средата: O(n)
 * foreach: удобство за обхождане (без индекс).
 * String.split(regex): връща String[], регекс " +" = един/повече интервали.
 * replaceAll(regex, repl): замяна на всички съвпадения.
 * trim(): маха начални/крайни whitespace.
 * Arrays.setAll(a, i->expr): попълване по индекс.
 * Streams: map/filter/reduce/collect – декларативен стил.
 * 2D масив: int[][] – “масив от масиви”, редовете може да са различни по дължина.
 * ========================== */

public class Lesson_2025_11_02_Detailed {

    public static void main(String[] args) {
        title("1) String обработка: replaceAll, trim, split, foreach");
        demoStringProcessing();

        title("2) Масиви + Random + setAll + streams + join + boxing");
        demoStreamsAndRandom();

        title("3) Филтриране и търсене в long[] (итеративно и със streams) + indexOf");
        demoFilterAndSearch();

        title("4) 2D масиви: печат/подравняване, размяна, суми/макс, transpose");
        demo2DArraySuite();

        title("5) Object[] → record RT (стойност + тип като текст)");
        demoRecordAndCreate();

        title("6) Допълнителни мини-задачи (min/max, сортиране, distinct, честоти, reduce)");
        demoExtras();

        title("Всичко приключи успешно ✅");
    }

    /* ------------------------------------------------------------
     * 1) String: replaceAll, trim, split, foreach
     * Вдъхновено от Project_05_06_Strings
     *
     * ЗАДАЧА: Има низ с '='. Заменяме блокове '=' с интервали, чистим краища,
     * сплит по интервали, печатаме частите с foreach.
     * Регекс:
     *   =+  -> един или повече '='
     *   " +" -> един или повече интервали
     * Сложност: O(n) за дължина на низа.
     * Edge cases: null низ (тук не ползваме), повече/по-малко интервали – покрито с " +".
     * ------------------------------------------------------------ */
    private static void demoStringProcessing() {
        String s = "===123-200=====7===";
        System.out.println("Оригинал: \"" + s + "\"");

        s = s.replaceAll("=+", "      "); // групи '=' → блок от интервали
        s = s.trim(); // махни крайни интервали
        System.out.println("След replaceAll+trim: \"" + s + "\"");

        String[] parts = s.split(" +"); // 1+ интервали
        System.out.println("Разбити части:");
        for (var elm : parts) {
            System.out.println("  → \"" + elm + "\"");
        }

        // Забележка: ако искаме да пазим разделителите, можем да ползваме Pattern/Matcher.
        // Тук целта е нормализиране към “tokens”.
    }

    /* ----------------------------------------------------------------
     * 2) Масиви + Random + Arrays.setAll + streams + join + boxing
     * Вдъхновено от Project_06
     *
     * Показваме:
     *  - Long[] vs long[] (обвивка vs примитив)
     *  - промяна “на място” с for (индекси)
     *  - генериране на long[] с Random.longs()
     *  - stream → mapToObj → joining(";")
     *  - boxed() за преобразуване към Long и обратно към масив
     * ---------------------------------------------------------------- */
    private static void demoStreamsAndRandom() {
        int[] ar1 = {1,2,3,4,5,6,7,8};
        System.out.println("int[] ar1 = " + Arrays.toString(ar1));

        Long[] ar2 = {10L, 20L, 3L};
        System.out.println("Long[] ar2 (начално) = " + Arrays.toString(ar2));

        // Модификация по индекс
        for (int i = 0; i < ar2.length - 1; ++i) {
            ar2[i] *= -2; // авто-распаковане/опаковане (un/boxing)
        }
        System.out.println("Long[] ar2 (след промяна) = " + Arrays.toString(ar2));

        Random rnd = new Random(12345); // фиксирано семе за повторяем резултат
        long[] ar3 = rnd.longs(7, 10, 12).toArray(); // само 10 или 11
        System.out.println("long[] ar3 = " + Arrays.toString(ar3));

        String joined = Arrays.stream(ar3)
                .mapToObj(Long::toString)
                .collect(Collectors.joining(";"));
        System.out.println("Joined ar3: " + joined);

        // Генерираме Long[] чрез boxed()
        Long[] ar2b = rnd.longs(7, -2, 34).boxed().toArray(Long[]::new);
        System.out.println("Long[] ar2b (boxed) = " + Arrays.toString(ar2b));

        // Arrays.setAll за пълнене на int[] в диапазон [-30, 1]
        int[] ar4 = new int[10];
        Arrays.setAll(ar4, i -> rnd.nextInt(-30, 2)); // горната граница е изключителна
        System.out.println("int[] ar4 (setAll) = " + Arrays.toString(ar4));

        // Бележка: Arrays.setAll е O(n). rnd.nextInt е O(1). Общата – O(n).
    }

    /* ------------------------------------------------------------------
     * 3) Филтриране/търсене в long[] – итеративно vs Streams + indexOf
     * Вдъхновено от Project_03_12 и Project_08
     *
     * Показваме:
     *  - filter (итеративно) – две минавания (преброяване + копиране)
     *  - filter2 (streams) – един pass вътрешно, но също O(n)
     *  - indexOf линейно търсене
     * Edge cases: null масив → празен резултат/-1
     * ------------------------------------------------------------------ */
    private static void demoFilterAndSearch() {
        Random rnd = new Random(2025);
        long[] nums = rnd.longs(12, -5, 12).toArray(); // [-5..11]
        System.out.println("long[] nums: " + Arrays.toString(nums));

        long n = 3L; // ще премахнем всички 3-ки
        System.out.println("Филтрираме всички различни от " + n);

        long[] filtered = filter(nums, n);
        System.out.println("filter(iterative)  = " + Arrays.toString(filtered));

        long[] filtered2 = filter2(nums, n);
        System.out.println("filter2(streams)   = " + Arrays.toString(filtered2));

        long target = 5L;
        int idx = indexOf(nums, target);
        System.out.println("indexOf(" + target + ") = " + idx);

        int idxStream = Arrays.stream(nums).boxed().toList().indexOf(target);
        System.out.println("indexOf чрез stream/boxed() = " + idxStream);
    }

    // Итеративен филтър: две минавания (преброяване + присвояване)
    public static long[] filter(long[] nums, long n) {
        if (nums == null) return new long[0];
        int len = 0;
        for (long elm : nums) if (elm != n) ++len;       // O(n)

        long[] result = new long[len];
        int i = 0;
        for (long elm : nums) if (elm != n) result[i++] = elm; // O(n)
        return result; // общо O(n)
    }

    // Streams филтър – също O(n), но по-декларативно
    public static long[] filter2(long[] nums, long n) {
        if (nums == null) return new long[0];
        return Arrays.stream(nums).filter(elm -> elm != n).toArray();
    }

    // Линеен indexOf – O(n)
    public static int indexOf(long[] arr, long target) {
        if (arr == null) return -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    /* ------------------------------------------------------------------
     * 4) 2D масиви: печат с подравняване, размяна на редове, суми/макс,
     * transpose (за правоъгълни матрици), защита за “назъбени” редове.
     * Вдъхновено от Project_05_06.print + твои примери за swap.
     * ------------------------------------------------------------------ */
    private static void demo2DArraySuite() {
        int[][] tabl = {
                {   1,    2,   33 },
                {  -7,  128,    3 },
                {   3,    4,    5 }
        };
        System.out.println("Оригинална таблица:");
        printTable(tabl);

        // Размяна на редове 0 ↔ 2
        swapRows(tabl, 0, 2);
        System.out.println("След swapRows(0,2):");
        printTable(tabl);

        // Суми по редове и колони
        System.out.println("Суми по ред:");
        int[] rowSums = rowSums(tabl);
        System.out.println("  " + Arrays.toString(rowSums));

        System.out.println("Суми по колона:");
        int[] colSums = colSums(tabl);
        System.out.println("  " + Arrays.toString(colSums));

        // Макс стойност и позиция
        int[] maxPos = argMax2D(tabl);
        System.out.println("Макс стойност = " + tabl[maxPos[0]][maxPos[1]]
                + " @ ред " + maxPos[0] + ", колона " + maxPos[1]);

        // Транспониране (ако всички редове са с еднаква дължина)
        int[][] transposed = transposeRectangular(tabl);
        System.out.println("Транспонирана матрица:");
        printTable(transposed);
    }

    public static void printTable(int[][] t) {
        if (t == null) return;
        // Намираме максималната “ширина” на елементите
        int w = 0;
        for (int[] row : t) {
            if (row == null) continue;
            for (int elm : row) {
                int len = Integer.toString(elm).length();
                if (len > w) w = len;
            }
        }
        w = Math.max(w + 2, 3);
        for (int r = 0; r < t.length; ++r) {
            int[] row = t[r];
            if (row == null) {
                System.out.println("<null row>");
                continue;
            }
            for (int c = 0; c < row.length; ++c) {
                System.out.printf("%" + w + "d", row[c]);
            }
            System.out.println();
        }
        System.out.println("=".repeat(w));
    }

    public static void swapRows(int[][] t, int r1, int r2) {
        if (t == null) return;
        if (r1 < 0 || r2 < 0 || r1 >= t.length || r2 >= t.length) return;
        int[] tmp = t[r1];
        t[r1] = t[r2];
        t[r2] = tmp; // O(1)
    }

    public static int[] rowSums(int[][] t) {
        if (t == null) return new int[0];
        int[] sums = new int[t.length];
        for (int i = 0; i < t.length; i++) {
            int s = 0;
            if (t[i] != null) {
                for (int v : t[i]) s += v;
            }
            sums[i] = s;
        }
        return sums;
    }

    public static int[] colSums(int[][] t) {
        if (t == null || t.length == 0) return new int[0];
        int maxCols = 0;
        for (int[] row : t) if (row != null) maxCols = Math.max(maxCols, row.length);
        int[] sums = new int[maxCols];
        for (int[] row : t) {
            if (row == null) continue;
            for (int c = 0; c < row.length; c++) sums[c] += row[c];
        }
        return sums;
    }

    // Връща позицията [r,c] на максималната стойност (първото срещане).
    public static int[] argMax2D(int[][] t) {
        if (t == null || t.length == 0) return new int[]{-1, -1};
        boolean init = false;
        int bestR = -1, bestC = -1, best = 0;
        for (int r = 0; r < t.length; r++) {
            int[] row = t[r];
            if (row == null || row.length == 0) continue;
            for (int c = 0; c < row.length; c++) {
                if (!init || row[c] > best) {
                    best = row[c];
                    bestR = r; bestC = c;
                    init = true;
                }
            }
        }
        return new int[]{bestR, bestC};
    }

    // Транспониране за правоъгълен (не “назъбен”) 2D масив
    public static int[][] transposeRectangular(int[][] t) {
        if (t == null || t.length == 0) return new int[0][0];
        int cols = t[0].length;
        for (int r = 1; r < t.length; r++) {
            if (t[r] == null || t[r].length != cols) {
                // Ако не е правоъгълна матрица – връщаме празно.
                return new int[0][0];
            }
        }
        int[][] res = new int[cols][t.length];
        for (int r = 0; r < t.length; r++) {
            for (int c = 0; c < cols; c++) {
                res[c][r] = t[r][c];
            }
        }
        return res;
    }

    /* ----------------------------------------------------------------
     * 5) Object[] → record RT (стойност + тип като String)
     * Вдъхновено от Project_03_14 – довършена имплементация.
     * Показва “map” от сурови обекти към структурирана форма.
     * ---------------------------------------------------------------- */
    public record RT(Object obj, String type) {}

    public static RT[] create(Object[] arr) {
        if (arr == null) return new RT[0];
        RT[] result = new RT[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Object o = arr[i];
            String type =
                (o == null) ? "null"
                : (o.getClass().isArray()
                    ? o.getClass().getSimpleName() + " (array)"
                    : o.getClass().getSimpleName());
            result[i] = new RT(o, type);
        }
        return result;
    }

    private static void demoRecordAndCreate() {
        Consumer<Integer> printer = i -> System.out.println("Consumer видя: " + i);
        printer.accept(42);

        Object[] aObj = { "absd", Long.valueOf(123), new int[3], null };
        RT[] mapped = create(aObj);
        System.out.println("Mapped RT[]:");
        for (RT rt : mapped) {
            System.out.println("  - value=" + String.valueOf(rt.obj()) + ", type=" + rt.type());
        }
    }

    /* ----------------------------------------------------------------
     * 6) Допълнителни мини-задачи: min/max, sort, distinct, честоти,
     * reduce (сума/произведение), сравнение loop vs stream.
     * ---------------------------------------------------------------- */
    private static void demoExtras() {
        int[] arr = {7, -2, 7, 5, 9, -2, 5, 5, 1};
        System.out.println("arr = " + Arrays.toString(arr));

        int min = Arrays.stream(arr).min().orElse(Integer.MAX_VALUE);
        int max = Arrays.stream(arr).max().orElse(Integer.MIN_VALUE);
        System.out.println("min=" + min + ", max=" + max);

        // Сортиране (in-place)
        int[] sorted = Arrays.copyOf(arr, arr.length);
        Arrays.sort(sorted);
        System.out.println("sorted = " + Arrays.toString(sorted));

        // distinct (запазва естествения ред от stream)
        int[] distinct = Arrays.stream(arr).distinct().toArray();
        System.out.println("distinct = " + Arrays.toString(distinct));

        // Честоти с HashMap (O(n))
        Map<Integer, Integer> freq = new HashMap<>();
        for (int v : arr) freq.merge(v, 1, Integer::sum);
        System.out.println("freq (loop) = " + freq);

        // Честоти със streams
        Map<Integer, Long> freq2 = Arrays.stream(arr)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("freq (streams) = " + freq2);

        // map/filter/reduce: сума на квадратите на положителните
        int sumSquaresPosLoop = 0;
        for (int v : arr) if (v > 0) sumSquaresPosLoop += v * v;
        int sumSquaresPosStream = Arrays.stream(arr).filter(v -> v > 0).map(v -> v*v).sum();
        System.out.println("Σ v^2 (v>0) loop=" + sumSquaresPosLoop
                + ", streams=" + sumSquaresPosStream);

        // reduce към произведение на различните положителни (пример с Long)
        long productDistinctPos = Arrays.stream(arr)
                .filter(v -> v > 0)
                .distinct()
                .mapToLong(v -> v)
                .reduce(1L, (a, b) -> a * b);
        System.out.println("Произведение на distinct положителни = " + productDistinctPos);
    }

    /* ---------------- помощник за заглавия ---------------- */
    private static void title(String t) {
        System.out.println();
        System.out.println("==== " + t + " ====");
    }
}
