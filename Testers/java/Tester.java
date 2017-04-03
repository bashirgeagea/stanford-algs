import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Tester {
  public static void main(String[] args) throws UnsupportedEncodingException, IOException {
    System.out.println("Testing your code's output with the expected output.");
    System.out.println();

    String className = args[0];
    System.out.println("Your class name => " + className);

    // Iterate over all the filenames in the command-line arguments.
    for (int i = 1; i < args.length; i++) {
      String inputFilename = args[i];
      // Infer the output filename from the input filename.
      String outputFilename = inputFilename.replaceAll("input", "output");

      // Get the result of calling the solution class's main() method on this file.
      String result = callMain(className, inputFilename);

      // Display the results of this test case file.
      System.out.println(" inputFilename => " + inputFilename);
      String expectedResult = null;
      expectedResult = new String(Files.readAllBytes(Paths.get(outputFilename)), "UTF-8");
      System.out.print("\texpectedResult => " + expectedResult);
      System.out.print("\t   your result => " + result);
      if (result.equals(expectedResult)) {
        System.out.println("\tYour result matches the test case.");
      } else {
        System.out.println("\tYour result DOES NOT matches the test case.");
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        scanner.close();
      }
      System.out.println();
    }
  }

  private static String callMain(String className, String filename) {
    try {
      // Initialize a Class and Method object to invoke.
      Class<?> c = Class.forName(className);
      Class<?>[] argTypes = new Class[] {String[].class};
      Method simpleSolution = c.getDeclaredMethod("main", argTypes);
      String[] args = {filename};

      // Capture and return the standard output of the main function.
      // Create a stream to hold the output
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      // IMPORTANT: Save the old System.out!
      PrintStream old = System.out;
      // Tell Java to use your special stream
      System.setOut(ps);

      simpleSolution.invoke(null, (Object) args);

      // Return System.out to the initial stream.
      System.out.flush();
      System.setOut(old);

      return baos.toString();
    } catch (ClassNotFoundException x) {
      x.printStackTrace();
    } catch (NoSuchMethodException x) {
      x.printStackTrace();
    } catch (IllegalAccessException x) {
      x.printStackTrace();
    } catch (InvocationTargetException x) {
      x.printStackTrace();
    }
    return null;
  }
}
