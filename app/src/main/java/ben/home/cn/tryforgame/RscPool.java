package ben.home.cn.tryforgame;

/**
 * Created by benhuang on 17-8-30.
 */

public final class RscPool {
    // Resource Pool
    private int _id;
    private int _PoolCount;

    public RscPool(){
        _id = 0;
        _PoolCount = 0;
    }

    public RscPool(int _id, int _PoolCount) {
        this._id = _id;
        set_PoolCount(_PoolCount);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_PoolCount() {
        return _PoolCount;
    }

    public boolean set_PoolCount(int _Pool) {
        if(_Pool != 2 && _Pool != 0){
            return false;
        }
        this._PoolCount = _Pool;
        return true;
    }

    public boolean chk_PoolCount(){
        if(_PoolCount > 0){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean consumePool(){
        if(this._PoolCount > 0){
            this._PoolCount = this._PoolCount - 1;
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "RscPool{" +
                "id=" + _id +
                ", PoolCount=" + _PoolCount +
                '}';
    }
}
