int debug(int a,int b){
	int c = a*2;
	return c*b;
}
int main()
{
    int a = 4;
    int b = 5;
    int c = debug(a,b);
    int d = debug(a,c);
    return 0;
}
