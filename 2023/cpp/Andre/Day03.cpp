#include <fstream>
#include <iostream>

namespace std {
    template<>
    struct hash<tuple<int, int>> {
        size_t operator()(const tuple<int, int>& key) const {
            return hash<size_t>()(hash<int>()(get<0>(key)) + (size_t) get<1>(key));
        }
    };
}

int part1() {
    std::ifstream file("input/day03.txt");

    if (!file.is_open()) {
        std::cerr << "file did not open.\n";
        return 1;
    }

    std::string line;

    char c;
    bool newNum = true;
    std::vector<int> nums = {};
    // locs to index in nums
    std::unordered_map<std::tuple<int,int>, int> numLocations;
    std::vector<std::tuple<int, int>> specialLocations;
    int lineNumber = 0;
    int lineLength;

    while (std::getline(file, line)) {
        lineLength = line.length();
        for (int i = 0; i < lineLength; i++) {
            c = line[i];
            if (c == '.') {
                newNum = true;
            } else if (std::isdigit(c)) {
                if (newNum) {
                    nums.push_back(0);   
                }
                newNum = false;
                nums.back() = 10 * nums.back() + (c - '0');
                numLocations[std::tuple<int,int>(lineNumber, i)] = nums.size() - 1;
            } else  {
                newNum = true;
                specialLocations.push_back(std::tuple<int, int>(lineNumber, i));
            }
        }
        newNum = true;
        lineNumber++;
    }

    std::unordered_map<int, bool> numSet;

    for (std::tuple<int, int> loc : specialLocations) {
        int row = std::get<0>(loc);
        int col = std::get<1>(loc);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                // skip center
                if (i == 0 && j == 0) {
                    continue;
                }
            if (row + i >= 0 && row + i < lineLength && col + j >= 0 && col + j < lineLength) {
                if (auto numLoc = numLocations.find(std::tuple<int, int>(row+i, col+j)); numLoc != numLocations.end()) {
                    numSet[numLoc->second] = true;
                } 
            }
            
            }
        }

        
    }
    int sum = 0;
    for (auto& [key, value] : numSet) {
        sum += nums[key];
    }

    std::cout << sum << std::endl;
    return 0;
}

int part2() {
    return 0;
}

int main() {
    return part1();
}