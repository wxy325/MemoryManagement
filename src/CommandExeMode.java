
public enum CommandExeMode {
	PRE_RANDOM, POST_RANDOM, RANDOM,SEQUENCE, END;
	public String toString()
	{
		String str = null;
		switch (this) 
		{
		case PRE_RANDOM:
			str = "ǰ��ַ���";
			break;
		case POST_RANDOM:
			str = "���ַ���";
			break;
		case RANDOM:
			str = "���";
			break;
		case SEQUENCE:
			str = "˳��ִ��";
			break;
		case END:
			str = "����";
			break;
		}
		return str;
		
	}
}
