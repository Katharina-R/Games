
class Hangman {
private:
	wstring art = L""
	"                              6 6 6 6 6 6 6 6 6 6 6 6 6 6 6\n"
	"                              6         5                 6\n"
	"                              6       5                   6\n"
	"                              6     5                     6\n"
	"                              6   5                     4 4 4\n"
	"                              6 5                      4     4\n"
	"                              6                         4 4 4\n"
	"                              6                           3\n"
	"                              6                           3\n"
	"                              6                     2 2 2 3 2 2 2\n"
	"                              6                           3\n"
	"                              6                           3\n"
	"                              6                           3\n"
	"                              6                         1   1\n"
	"                              6                       1       1\n"
	"                              6                     1           1\n"
	"                              6\n"
	"                              6\n"
	"                              6\n"
	"                              6\n"
	"                        7           7\n"
	"                 7                        7\n"
	"            7                                  7\n"
	"        7                                          7\n"
	"     7                                                7\n"
	"   7                                                    7\n"
	" 7                                                        7\n"
	"7                                                          7\n";

public:
	void print(int lives, int max_life){
		if(lives == max_life) return;

		wstring art_cur = L"\n";
		for(char c : art){
			if('1' <= c && c <= '7'){
				if(lives < c - '0'){
					art_cur += 'x';
				}
				else {
					art_cur += ' ';
				}
			}
			else {
				art_cur += c;
			}
		}

		wcout << art_cur << endl;			
	}
};