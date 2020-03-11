package corretor;

import montador.Pass;
import montador.Pass1;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by deborasetton on 14/03/16.
 */
public class Pass1Corretor {

    public int passCount = 0;
    public int failCount = 0;
    private Pass1 pass1 = new Pass1();

    public void corrigir() {

        try {
            testDefineNewOrigin();
            testDefineConstant();
            testEndAsm();
            testReserveBlock();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        double nota = ((float) (passCount)) / (passCount + failCount);
        System.out.printf("Nota: %.3f\n", nota);
    }

    private void testDefineNewOrigin() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method m = Pass1.class.getDeclaredMethod("defineNewOrigin", String.class);
        m.setAccessible(true);

        boolean result = (boolean)m.invoke(pass1, "/250");
        if (result) {
            System.out.printf("[v] [testDefineNewOrigin] Nova origem definida com sucesso\n");
            passCount++;
        } else {
            System.out.printf("[x] [testDefineNewOrigin] Retornou falso para uma origem válida\n");
            failCount++;
        }

        Field field = Pass.class.getDeclaredField("locationCounter");
        field.setAccessible(true);
        int locationCounter = (int)field.get(pass1);

        if (locationCounter == 592) {
            System.out.printf("[v] [testDefineNewOrigin] Nova origem definida corretamente\n");
            passCount++;
        } else {
            System.out.printf("[x] [testDefineNewOrigin] Esperado: locationCounter = 10. Obtido: %d\n", locationCounter);
            failCount++;
        }
    }

    private void testDefineConstant() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

//        locationCounter+=2;
//        if(!(locationCounter > LAST_VAL_ADDR)){
//            return true;
//        }
//        return false;

        Method m = Pass1.class.getDeclaredMethod("defineConstant");
        m.setAccessible(true);

        boolean result = (boolean)m.invoke(pass1);
        if (result) {
            System.out.printf("[v] [testDefineConstant] Constante definida com sucesso\n");
            passCount++;
        } else {
            System.out.printf("[x] [testDefineConstant] Retornou falso para definição de constante\n");
            failCount++;
        }

        Method m2 = Pass1.class.getDeclaredMethod("defineNewOrigin", String.class);
        m2.setAccessible(true);

        // Define new origin.
        m2.invoke(pass1, "/1000");

        // Define a constant.
        result = (boolean)m.invoke(pass1);

        if (!result) {
            System.out.printf("[v] [testDefineConstant] Retornou falso para a definição de constante quando não havia mais espaço\n");
            passCount++;
        } else {
            System.out.printf("[x] [testDefineConstant] Retornou verdadeiro para a definição de constante quando não havia mais espaço\n");
            failCount++;
        }
    }

    private void testEndAsm() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method methodEndAsm = Pass1.class.getDeclaredMethod("endAsm", String.class);
        methodEndAsm.setAccessible(true);

        Method methodDefineNewOrigin = Pass1.class.getDeclaredMethod("defineNewOrigin", String.class);
        methodDefineNewOrigin.setAccessible(true);

        // Define new origin.
        methodDefineNewOrigin.invoke(pass1, "/0FFA");

        boolean result = (boolean)methodEndAsm.invoke(pass1, "X");
        if (result) {
            System.out.printf("[v] [testEndAsm] Ok\n");
            passCount++;
        } else {
            System.out.printf("[x] [testEndAsm] Retornou falso quando o locationCounter estava ainda em uma área válida\n");
            failCount++;
        }

        // Define new origin.
        methodDefineNewOrigin.invoke(pass1, "/1002");

        // Define a constant.
        result = (boolean)methodEndAsm.invoke(pass1, "X");

        if (!result) {
            System.out.printf("[v] [testEndAsm] Ok\n");
            passCount++;
        } else {
            System.out.printf("[x] [testEndAsm] Retornou falso quando o locationCounter não estava em uma área válida\n");
            failCount++;
        }
    }

    private void testReserveBlock() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

//        try{
//            locationCounter+= 2*getDecNumber(arg);
//            return true;
//        }catch(Exception e){
//            System.err.println(e.getMessage());
//        }
//        return false;

        Method methodReserveBlock = Pass1.class.getDeclaredMethod("reserveBlock", String.class);
        methodReserveBlock.setAccessible(true);

        Method methodDefineNewOrigin = Pass1.class.getDeclaredMethod("defineNewOrigin", String.class);
        methodDefineNewOrigin.setAccessible(true);

//        // Define new origin.
//        methodDefineNewOrigin.invoke(pass1, "/0FFA");

        methodDefineNewOrigin.invoke(pass1, "/0");
        boolean result = (boolean)methodReserveBlock.invoke(pass1, "/10");

        if (result) {
            System.out.printf("[v] [testReserveBlock] Ok\n");
            passCount++;
        } else {
            System.out.printf("[x] [testReserveBlock] reserveBlock() retornou falso incorretamente\n");
            failCount++;
        }

        Field field = Pass.class.getDeclaredField("locationCounter");
        field.setAccessible(true);
        int locationCounter = (int)field.get(pass1);

        if (locationCounter == 32) {
            System.out.printf("[v] [testReserveBlock] Ok\n");
            passCount++;
        } else {
            System.out.printf("[x] [testReserveBlock] Esperado: locationCounter = 16. Obtido: %d\n", locationCounter);
            failCount++;
        }

        // Define new origin.
        methodDefineNewOrigin.invoke(pass1, "/500");

        // Define a constant.
        result = (boolean)methodReserveBlock.invoke(pass1, "/16");

        locationCounter = (int)field.get(pass1);

        if (locationCounter == 1324) {
            System.out.printf("[v] [testReserveBlock] Ok\n");
            passCount++;
        } else {
            System.out.printf("[x] [testReserveBlock] Esperado: locationCounter = 1302. Obtido: %d\n", locationCounter);
            failCount++;
        }
    }

}
