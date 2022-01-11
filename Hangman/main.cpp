#include "alphabet.cpp"

using namespace std;

void clearConsole(){
	wcout << "\x1B[2J\x1B[H";
}

bool isUserConfirm(){
	wstring confirm;
	wcin >> confirm;
	return confirm.compare(L"yes") == 0 || confirm == L"y";
}

Alphabet getAlphabet(){
	wcout << "The default alphabet is: ";
	for(wchar_t c : ALPHABET) wcout << c << " ";
	wcout << endl;

	wcout << "Please enter all special characters you want to add: ";
	wstring specialChars;
	getline(wcin, specialChars);

	Alphabet alphabet(specialChars); 
	alphabet.print();

	return alphabet;
}

wstring getSecretWord(const Alphabet& alphabet){
	wstring secretWord;

	while(true) {
		// Read secret word
		wcout << "\n\nChoose a secret word: ";
		wcin >> secretWord;
		transform(secretWord.begin(), secretWord.end(), secretWord.begin(), towupper);
		
		// Check secret word
		if(!alphabet.isValid(secretWord)){
			alphabet.print();
			continue;
		}

		// Ask user to confirm
		wcout << "Your secret word is " << secretWord << ". Is that correct (yes/no)? ";
		if(isUserConfirm()) break;
	}

	clearConsole();
	wcout << "A secret word has been chosen!" << endl;

	return secretWord;
}

void printSecretWord(const wstring& secretWord, const Alphabet& alphabet){
	wcout << "The secret word is: ";
	for(wchar_t c : secretWord){
		if(alphabet.isUsed(c) == Used::YES){
			wcout << c << " ";
		}
		else {
			wcout << "_ ";
		}
	}
	cout << endl;
}

wchar_t getLetter(const Alphabet& alphabet){
	wchar_t letter;

	while(true){
		wcout << "\n\nChoose a letter: ";
		wcin >> letter;

		if(alphabet.isValid(letter)) break;
		else wcout << letter << "is not a valid letter!";
	}

	return letter;
}

bool isGameOver(int lives, const wstring& secretWord, const Alphabet& alphabet){
	// Game lost
	if(lives < 1){
		wcout << "You lost!" << endl;
		return true;
	}

	// Game won
	for(wchar_t c : secretWord){
		if(alphabet.isUsed(c) != Used::YES) return false;
	}

	// Game in progress
	wcout << "You won!" << endl;
	return true;
}

bool isPlayAgain(){
	wcout << "Do you want to play again (yes/no)? ";
	return isUserConfirm();
}

// g++ -std=gnu++2a -o main main.cpp
int main(){
	// Set up locale
	ios_base::sync_with_stdio(false);
	locale::global(locale("en_US.UTF8"));
	wcin.imbue(locale());
    wcout.imbue(locale());

	// Set up alphabet
	Alphabet alphabet = getAlphabet();

	while(true){
		int lives = 10;
		wstring secretWord = getSecretWord(alphabet);

		// Game loop
		for(int i = 1; ; i++) {
			wcout << "\n\nRound " << i << endl;
			wcout << "You have " << lives << L" hearts" << endl;
			alphabet.print();
			printSecretWord(secretWord, alphabet);

			wchar_t letter = getLetter(alphabet);

			if(secretWord.find(towupper(letter)) != wstring::npos) {
				alphabet.mark(letter, Used::YES);
			}
			else {
				lives--;
				alphabet.mark(letter, Used::NO);
			}

			if(isGameOver(lives, secretWord, alphabet)) {
				wcout << "The secret word was " << secretWord << endl;
				break;
			}
		}

		if(!isPlayAgain()) break;
		clearConsole();
		alphabet.clearUsed();
	}
}