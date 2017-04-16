#!/bin/sh

# author: Professores de Sistemas Distribuídos - Instituto Superior Técnico
################################################################################
# Script to zip a collection of folders with a password
# usage:
#     ./zip-with-password.sh <users-file> <folder>
# The users file contains a list with <usarname password> entries
################################################################################

folder=$2


while read line; do
  username=${line%% *}
  username=$(echo $username | tr 'a-z' 'A-Z')
  password=${line##* }
  folderToZip=$folder/$username

  echo "Zipping $folderToZip with password '$password'..."
  cd $folder
  zip -P $password -r $username.zip $username
  cd ..
done < $1
