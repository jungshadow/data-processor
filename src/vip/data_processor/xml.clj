(ns vip.data-processor.xml
  "Provide safer versions of unsafe clojure.data.xml public functions that are
  vulnerable to
  [XML external entity attacks](https://en.wikipedia.org/wiki/XML_external_entity_attack).

  This namespace should be removed when clojure.data.xml is upgraded beyond
  version 0.0.8"
  (:require [clojure.data.xml :as xml]))

(def ^:private safer-defaults
  "Defaults for old versions of `clojure.data.xml` to avoid XXE."
  {:supporting-external-entities false
   :support-dtd false})

(defn source-seq
  "See [[xml/source-seq]]."
  [s & {:as props}]
  (apply xml/source-seq s (apply concat (merge safer-defaults props))))

(defn parse
  "See [[xml/parse]]."
  [s & {:as props}]
  (apply xml/parse s (apply concat (merge safer-defaults props))))

(defn parse-str
  "See [[xml/parse-str]]."
  [s & {:as props}]
  (apply xml/parse-str s (apply concat (merge safer-defaults props))))
