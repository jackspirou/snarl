//  ALLOCATOR.

public class Allocator
{

//  REGISTER.

  public final class Register
  {
    private String   name;  //  Printable name of this REGISTER.
    private Register next;  //  Next REGISTER in stack of REGISTERS.
    private boolean  used;  //  Has this REGISTER been REQUESTed?

//  Constructor.

    private Register()
    { }

//  Constructor.

    private Register(String name, Register next, boolean used)
    {
      this.name = name;
      this.next = next;
      this.used = used;
    }

//  IS USED.

    public boolean isUsed()
    {
      return used;
    }

//  TO STRING.

    public String toString()
    {
      return name;
    }
  }

//  Special registers not subject to allocation.

  public final Register fp   = new Register("$fp",   null, true);
  public final Register ra   = new Register("$ra",   null, true);
  public final Register sp   = new Register("$sp",   null, true);
  public final Register v0   = new Register("$v0",   null, true);
  public final Register zero = new Register("$zero", null, true);

  private Register registers;  //  Stack of REGISTERs to be allocated.
  private Source   source;     //  So we can call SOURCE.ERROR.

//  Constructor.

  public Allocator(Source source)
  {
    registers = null;
    for (int index = 7; index >= 0; index -= 1)
    {
      registers = new Register("$s" + index, registers, false);
    }
    this.source = source;
  }

//  REQUEST.

  public Register request()
  {
    if (registers == null)
    {
      source.error("Expression too complex.");
      return null;
    }
    else
    {
      Register register = registers;
      registers = registers.next;
      register.used = true;
      return register;
    }
  }

//  RELEASE.

  public void release(Register register)
  {
    if (register.used)
    {
      register.used = false;
      register.next = registers;
      registers = register;
    }
    else
    {
      throw new RuntimeException("Can't release " + register + ".");
    }
  }

//  MAIN.

  public static void main(String[] args)
  {
    Allocator allocator = new Allocator(new Source("example.snarl"));

    System.out.println(allocator.fp);
    System.out.println(allocator.ra);
    System.out.println(allocator.sp);
    System.out.println(allocator.v0);
    System.out.println(allocator.zero);

    Register reg0 = allocator.request(); System.out.println(reg0);
    Register reg1 = allocator.request(); System.out.println(reg1);
    Register reg2 = allocator.request(); System.out.println(reg2);
    Register reg3 = allocator.request(); System.out.println(reg3);
    Register reg4 = allocator.request(); System.out.println(reg4);
    Register reg5 = allocator.request(); System.out.println(reg5);
    Register reg6 = allocator.request(); System.out.println(reg6);
    Register reg7 = allocator.request(); System.out.println(reg7);

    allocator.release(reg0);
    allocator.release(reg1);
    allocator.release(reg2);
    allocator.release(reg3);
    allocator.release(reg4);
    allocator.release(reg5);
    allocator.release(reg6);
    allocator.release(reg7);

//  allocator.release(reg0);

    reg0 = allocator.request(); System.out.println(reg0);
    reg1 = allocator.request(); System.out.println(reg1);
    reg2 = allocator.request(); System.out.println(reg2);
    reg3 = allocator.request(); System.out.println(reg3);
    reg4 = allocator.request(); System.out.println(reg4);
    reg5 = allocator.request(); System.out.println(reg5);
    reg6 = allocator.request(); System.out.println(reg6);
    reg7 = allocator.request(); System.out.println(reg7);
   }
}
