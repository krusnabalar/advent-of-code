#include <fstream>
#include <iostream>



int part1() {
    int maxRed = 12;
    int maxGreen = 13;
    int maxBlue = 14;

    std::ifstream file("input/Day02.txt");

    if (!file.is_open()) {
        std::cerr << "File was not opened." << std::endl;
        return 1;
    }

    std::string line;
    int number;
    int id = 1;
    int idSum = 0;
    while (std::getline(file, line)) {
        // assume
        bool possible = true;

        number = 0;
        for(int i = 6; i < line.length(); i++) {
            char c = line[i];

            if (std::isdigit(c)) {
                number *= 10;
                number += c - '0';
                continue;
            } else if (c != ' ') {
                if (c == 'r') {
                    std::string red = "red";
                    i += red.length();
                    if (number > maxRed) {
                        possible = false;
                        std::cout << id << ": too many reds " << number << " at index " << i << "\n";
                        break;
                    }
                } else if (c == 'g') {
                    std::string green = "green";
                    i += green.length();
                    if (number > maxGreen) {
                        possible = false;
                        std::cout << id << ": too many greens " << number << " at index " << i << "\n";
                        break;
                    }
                } else if (c == 'b') {
                    std::string blue = "blue";
                    i += blue.length();
                    if (number > maxBlue) {
                        possible = false;
                        std::cout << id << ": too many blues " << number << " at index " << i << "\n";
                        break;
                    }
                }
                number = 0;
            }
            
        }

        if (possible) {
            std::cout << id << ": possible\n";
            idSum += id;
        }
        id++;
    }

    std::cout << idSum << "\n";


    return 0;
}

int part2() {

    std::ifstream file("input/Day02.txt");

    if (!file.is_open()) {
        std::cerr << "File was not opened." << std::endl;
        return 1;
    }

    std::string line;
    int number;
    int sum = 0;
    while (std::getline(file, line)) {

        std::cout << "reset" << "\n";
        int minRed = 0;
        int minGreen = 0;
        int minBlue = 0;

        number = 0;
        for(int i = 6; i < line.length(); i++) {
            char c = line[i];

            if (std::isdigit(c)) {
                number *= 10;
                number += c - '0';
                continue;
            } else if (c != ' ') {
                if (c == 'r') {
                    std::string red = "red";
                    i += red.length();
                    if (number > minRed) {
                        minRed = number;
                        std::cout << number << "\n";
                    }
                } else if (c == 'g') {
                    std::string green = "green";
                    i += green.length();
                    if (number > minGreen) {
                        minGreen = number;
                        std::cout << number << "\n";
                    }
                } else if (c == 'b') {
                    std::string blue = "blue";
                    i += blue.length();
                    if (number > minBlue) {
                        minBlue = number;
                        std::cout << number << "\n";
                    }
                }
                number = 0;
            }
            
        }
        sum += minRed * minGreen * minBlue;
    }

    std::cout << sum << "\n";


    return 0;
}

int main() {
    return part2();
}