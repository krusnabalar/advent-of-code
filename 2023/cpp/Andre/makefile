# Compiler settings
CXX = g++
CXXFLAGS = -Wall -std=c++17
# Get all cpp files in the directory
SOURCES = $(wildcard *.cpp)
# Define corresponding executables
EXECUTABLES = $(patsubst %.cpp,bin/%,$(SOURCES))
# Default target
all: $(EXECUTABLES)
# Rule to create the bin directory
bin:
	@mkdir -p bin
# Rule to build executables
bin/%: %.cpp | bin
	$(CXX) $(CXXFLAGS) $< -o $@
# Clean rule
clean:
	rm -f $(EXECUTABLES)