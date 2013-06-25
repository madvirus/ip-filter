package org.chimi.ipfilter.perf;

import java.util.Iterator;

public class IpIterator implements Iterator<String> {

    private class IpValue {
        private int v1;
        private int v2;
        private int v3;
        private int v4;

        public IpValue(String ipString) {
            String[] ips = ipString.split("\\.");
            v1 = Integer.parseInt(ips[0]);
            v2 = Integer.parseInt(ips[1]);
            v3 = Integer.parseInt(ips[2]);
            v4 = Integer.parseInt(ips[3]);
        }

        @Override
        public String toString() {
            return v1 + "." + v2 + "." + v3 + "." + v4;
        }

        public boolean lessThanOrEquals(IpValue that) {
            if (this.v1 < that.v1) return true;
            else if (this.v1 > that.v1) return false;

            // v1이 같으면
            if (this.v2 < that.v2) return true;
            else if (this.v2 > that.v2) return false;

            // v1, v2가 같으면
            if (this.v3 < that.v3) return true;
            else if (this.v3 > that.v3) return false;

            // v1, v2, v3가 같으면
            if (this.v4 <= that.v4) return true;
            return false;
        }

        public void increase() {
            v4++;
            if (v4 == 256) {
                v4 = 0;
                v3++;

                if (v3 == 256) {
                    v3 = 0;
                    v2++;

                    if (v2 == 256) {
                        v2 = 0;
                        v1++;
                    }
                }
            }
        }
    }

    private IpValue currentIpValue;
    private IpValue toIpValue;

    public IpIterator(String from, String to) {
        currentIpValue = new IpValue(from);
        toIpValue = new IpValue(to);
    }

    @Override
    public boolean hasNext() {
        return currentIpValue.lessThanOrEquals(toIpValue);
    }

    @Override
    public String next() {
        String value = currentIpValue.toString();
        currentIpValue.increase();
        return value;
    }

    @Override
    public void remove() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
