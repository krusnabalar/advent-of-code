#include <fstream>
#include <iostream>
#include <cctype>
#include <optional>



int part1() {
    std::ifstream file("input/Day01.txt");
    if (!file.is_open()) {
        std::cerr << "File did not open." << std::endl;
        return 1;
    }

    int sum = 0;

    std::string line;
    while(std::getline(file, line)) {
        std::optional<char> first;
        std::optional<char> last;

        for (char c : line) {
            if (std::isdigit(c)) {
                if (!first) {
                    first = c;

                }
                last = c;
            }
        }
        int number = (*first - '0') * 10 + (*last - '0');
        std::cout << number << std::endl;
        sum += number;
    }

    file.close();

    std::cout << sum << std::endl;

    return 0;
}

int part2() {
    std::array<std::string, 9> strNums = { 
            "one", 
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
        };

    std::ifstream file("input/Day01.txt");
    if (!file.is_open()) {
        std::cerr << "File did not open." << std::endl;
        return 1;
    }

    int sum = 0;    

    std::string line;
    while(std::getline(file, line)) {
        std::optional<int> first;
        std::optional<int> last;

        

        for (int i = 0; i < line.length(); i++) {
            char c = line[i];
            if (std::isdigit(c)) {
                if (!first) {
                    first = c - '0';
                }
                last = c - '0';
            } else {
                for (int j = 0; j < strNums.size(); j++) {
                    if (line.substr(i, strNums[j].length()) == strNums[j]) {
                        if (!first) {
                            first = j + 1;
                        }
                        last = j + 1;
                        i += strNums[j].length() - 1;
                        break;
                    } 
                }
            }
        }
        int number = (*first) * 10 + (*last);
        sum += number;
    }

    file.close();

    std::cout << sum << std::endl;

    return 0;
}

int main() {
    return part2();
}