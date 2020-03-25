#!/bin/bash

# Cores *-*
COLOR_RED='\033[0;31m'
COLOR_GREEN='\033[0;32m'
COLOR_YELLOW='\033[0;33m'
COLOR_BLUE='\033[0;34m'
COLOR_END='\033[0m'

p_info()
{
  echo -e "${COLOR_BLUE}$*${COLOR_END}"
}

p_err()
{
  echo -e "${COLOR_RED}$*${COLOR_END}"
}

p_ok()
{
  echo -e "${COLOR_GREEN}$*${COLOR_END}"
}
