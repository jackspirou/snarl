class Set
{
  public static final int maxElement = 63;

  public static long makeSet(int [] elements)
  { long set = 0;
    for (int index = 0; index < elements.length; index += 1)
    { set |= (1L << elements[index]); }
    return set; }

  public static long makeSet(int... elements)
  { long set = 0;
    for (int element: elements)
    { set |= (1L << element); }
    return set; }

  public static boolean isIn(int element, long set)
  { return (set & (1L << element)) != 0; }

  public static long difference(long leftSet, long rightSet)
  { return leftSet & ~ rightSet; }

  public static long intersection(long leftSet, long rightSet)
  { return leftSet & rightSet; }

  public static long union(long leftSet, long rightSet)
  { return leftSet | rightSet; }

  public static void writeSet(long set)
  { int element = 0;
    String delimiter = " ";
    System.out.print('{');
    while (element <= maxElement)
    { if (isIn(element, set))
      { System.out.print(delimiter);
        System.out.print(element);
        delimiter = ", "; }
      element += 1; }
    System.out.print(" }"); }

  public static void writelnSet(long set)
  { writeSet(set);
    System.out.println(); }

  public static void main(String [] args)
  { long test0 = makeSet(new int[] { 1, 2, 3, 4 });
    long test1 = makeSet(1, 2, 3, 4);
    writelnSet(test0);
    writelnSet(test1);

    long left  = makeSet1(1, 2, 3);
    long right = makeSet1(2, 3, 4);
    writelnSet(difference(left, right));    //  { 1 }
    writelnSet(intersection(left, right));  //  { 2, 3 }
    writelnSet(union(left, right)); }       //  { 1, 2, 3, 4 }
}
