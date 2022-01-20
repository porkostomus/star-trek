(ns trek
  (:require [cheshire.core :as json]
            [clojure.string :as str]
            [clojure.data.xml :as xml]))

(defn fetch-ep [s e]
  (slurp (str "https://www.springfieldspringfield.co.uk/view_episode_scripts.php?tv-show=star-trek-discovery-2017&episode=s" s "e" e)))

(doseq [s ["04"]
        e ["01" "02" "03" "04" "05" "06" "07" "08" "09" "10" "11" "12" "13" "14" "15"]]
  (spit (str "disco-s" s "e" e) (fetch-ep s e)))

["s01e90" "s03e00"]

(spit "test" (fetch-ep "01" "01"))


(xml/parse-str 
 (-> (slurp  "disco-s01e01")
     (str/escape {\& "&amp;"})
     (str/replace "<script async src=" " <script async=\"async\" src=")))

(defn disco-seq [e s]
  (str/split (last (str/split (slurp  (str "disco-s" s "e" e))
                              #"<div class=\"scrolling-script-container\">"))
             #"<br>"))

(disco-seq "01" "02")

(defn episode [series ep]
  (get-in (json/parse-string (slurp "resources/all_scripts_raw.json"))
          [series ep]))

(defn script-seq 
  "Returns a seq of alternating characters and their lines."
  [series ep]
  (interleave
   (conj (re-seq #"[A-Z]+:" (episode series ep)) nil)
   (str/split (episode series ep) #"[A-Z]+:")))

(script-seq "TOS" "episode 0")
