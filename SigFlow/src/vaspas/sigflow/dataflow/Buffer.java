package vaspas.sigflow.dataflow;

import java.util.LinkedList;

public abstract class Buffer implements Channel {
	
	private final LinkedList<BufferItem> _buffers = new LinkedList<BufferItem>();
    
	private int _availableLength;

	protected abstract Object CreateArray(int length);

    public boolean ReadTo(Object data, int length)
    {
        if (_availableLength < length)
            return false;
        

        int readed = 0;
        while (readed < length)
        {
            int toRead = length - readed;
            
            BufferItem nextBuffer = _buffers.peek();

            if(toRead<_buffers.peek().getAvailable())
            {
                System.arraycopy(nextBuffer.Data, nextBuffer.From , data, readed , toRead );
                nextBuffer.From += toRead;
                readed += toRead;
            }
            else
            {
                System.arraycopy(nextBuffer.Data, nextBuffer.From, data, readed, nextBuffer.getAvailable());
                readed += nextBuffer.getAvailable();
                _pool.add(_buffers.poll());
            }
        }

        _availableLength-=length;
        
        return true;
    }
     
    public int getAvailable()
    {
        return _availableLength;
    }

    public int getNextBlockSize()
    {
    	BufferItem bi= _buffers.peek();
    	return bi==null?0:bi.Length;
    }

    private BufferItem _taked;
    public Object Take()
    {
    	BufferItem bi=_buffers.peek();
    	
        if (bi==null)
            return null;

        if(bi.From>0)
        {
        	int l=bi.getAvailable();
            Object newData = CreateArray(l);
            ReadTo(newData, l);
            _availableLength-=l;
            return newData;
        }

        _taked = _buffers.poll();
        _availableLength-=_taked.Length;
         return _taked.Data;
    }

    public void Put(Object data)
    {
        if(_taked.Data==data)
        {
            _pool.add(_taked);
            _taked = null;
        }
    }

    public void Write(Object data, int length)
    {
        BufferItem item = GetItem(length);
        
        _availableLength+=length;

        System.arraycopy(data, 0, item.Data, 0, length);

        item.From = 0;

        _buffers.add(item);
    }

    private final LinkedList<BufferItem> _pool = new LinkedList<BufferItem>();

    private BufferItem GetItem(int length)
    {
    	BufferItem bi=_pool.poll();
    	while(bi!=null)
    	{
    		if(bi.Length==length)
    		{
    			_pool.remove(bi);
                return bi;
    		}
    		bi=_pool.poll();
    	}
    	

        bi=new BufferItem();
        bi.Data= CreateArray(length);
        bi.Length=length;
        return bi;
    }

    public void TrySkip(int size)
    {
        int skipped = 0;
        while (skipped < size && _buffers.peek()!=null)
        {
            int toSkip = size - skipped;

            if (toSkip < _buffers.peek().getAvailable())
            {
                _buffers.peek().From += toSkip;
                skipped += toSkip;
            }
            else
            {
                skipped += _buffers.peek().getAvailable();
                _pool.add(_buffers.poll());
            }
        }
    }
}
