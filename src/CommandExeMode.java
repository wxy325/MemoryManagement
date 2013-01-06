
public enum CommandExeMode {
	PRE_RANDOM, POST_RANDOM, RANDOM,SEQUENCE, END;
	public String toString()
	{
		String str = null;
		switch (this) 
		{
		case PRE_RANDOM:
			str = "前地址随机";
			break;
		case POST_RANDOM:
			str = "后地址随机";
			break;
		case RANDOM:
			str = "随机";
			break;
		case SEQUENCE:
			str = "顺序执行";
			break;
		case END:
			str = "结束";
			break;
		}
		return str;
		
	}
}
