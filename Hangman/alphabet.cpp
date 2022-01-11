#include <iostream>
#include <locale>
#include <map>
#include <string>
#include <vector>

using namespace std;

enum Used {YES, NO, MAYBE};

const vector<wchar_t> ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
const wstring RED = L"\033[31m";
const wstring BLACK = L"\033[0m";
const wstring GREEN = L"\033[32m";

class Alphabet {
 private:
  map<wchar_t, Used> chars;
  map<wchar_t, Used> specialChars;

 public:
  Alphabet(wstring specialChars_){
  	for(wchar_t c : ALPHABET) chars[c] = Used::MAYBE;
  		for(wchar_t c : specialChars_) {
  			c = towupper(c);
  			if(!isValid(c)){
  				specialChars[c] = Used::MAYBE;
  			}
  		}
  	}

	bool isValid(const wchar_t c_unsafe) const {
		wchar_t c = towupper(c_unsafe);
		if(chars.contains(c) || specialChars.contains(c)) return true;

		return false;
	}

	bool isValid(const wstring& word) const{
		bool valid = true;
		for(wchar_t c_unsafe : word){
			wchar_t c = towupper(c_unsafe);
			if(!isValid(c)) {
				wcout << c << " is not a valid character!" << endl;
				valid = false;
			}
		}
		return valid;
	}

	void mark(wchar_t c_unsafe, Used used){
		wchar_t c = towupper(c_unsafe);
		if(chars.contains(c)) chars[c] = used;
		if(specialChars.contains(c)) specialChars[c] = used;
	}

	Used isUsed(const wchar_t c_unsafe) const{
		wchar_t c = towupper(c_unsafe);
		if(chars.contains(c)) return chars.at(c);
		return specialChars.at(c);
	}

	void clearUsed() {
		for(auto& p : chars) p.second = Used::MAYBE;
		for(auto& p : specialChars) p.second = Used::MAYBE;
	}

	wstring colour(const Used& used) const{
		switch(used){
			case YES: return GREEN;
			case NO: return RED;
			default: return BLACK;
		}
	}

	void print() const{
		wcout << "The alphabet is: ";
		for(const auto& p : chars) wcout << colour(p.second) << p.first << " ";
		for(const auto& p : specialChars) wcout << colour(p.second) << p.first << " ";
		wcout << BLACK << endl;
	}
};
