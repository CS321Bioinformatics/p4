package src;

public class RAM {

    //TODO Converts DNA substrings from file for GeneBankSearch driver

    public long getKey() {
        return key;
    }

    private long key = 0;




    public long convertGBKtoSubseq(String input)
    {
        input = input.toUpperCase();
        long key = 0x00000000;
        int tbits = 0b0;
        int position = 0;
        char c;
        for(int i = 0; i<input.length(); i++){
            //convert String char to 0,1,2,3
            c=input.charAt(i);
            switch (c){
                case 'A':
                    tbits = 0;
                    break;
                case 'C':
                    tbits = 1;
                    break;
                case 'T':
                    tbits = 3;
                    break;
                case 'G':
                    tbits = 2;
                    break;
            }
            // sequence: "ACTG" = 0x00|01|11|10
            key = (key << (2));
            key |= tbits;
        }
        return key;
    }


    public String convertLongtoString(long input, int length)
    {
        String result = ""; //append to end
        long temp;

        for(int i = 0; i<length; i++){ //get 2 bits from string
            temp = key;
            temp = (temp>>(2*i)); //normalize
            temp &= 0x03; //isolate 2 bits, and clear padded 0's
            if(temp == 00){ //A
                result+="a";
            }
            else if(temp == 01){ //C
                result+="c";
            }
            else if(temp == 11){ //T
                result+="t";
            }
            else if(temp == 10){ //G
                result+="g";
            }
        }

        return result;

    }

}