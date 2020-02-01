package libs;

import org.apache.beam.sdk.transforms.DoFn;


public class utils {


    public static class printElements extends DoFn<Object, Void> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            String str = context.element().toString();
            System.out.print("Element: ");
            System.out.println(str);
        }
    }

}