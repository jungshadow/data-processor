(ns vip.data-processor.validation.data-spec-test
  (:require [clojure.test :refer :all]
            [vip.data-processor.validation.data-spec :refer :all]
            [vip.data-processor.test-helpers :refer :all]
            [vip.data-processor.validation.csv :as csv]
            [vip.data-processor.validation.csv.value-format :as format]
            [vip.data-processor.db.sqlite :as sqlite]))

(deftest create-format-rule-test
  (let [column "id"
        filename "test.txt"
        line-number 7
        ctx {}]
    (testing "required column"
      (let [format-rule (create-format-rule filename {:name column :required true :format format/all-digits})]
        (testing "if the required column is missing, adds a fatal error"
          (let [result-ctx (format-rule ctx {column ""} line-number)]
            (is (get-in result-ctx [:fatal filename line-number column]))))
        (testing "if the column doesn't have the right format, adds an error"
          (let [result-ctx (format-rule ctx {column "asdf"} line-number)]
            (is (get-in result-ctx [:errors filename line-number column]))))
        (testing "if the required column is there and matches the format, is okay"
          (let [result-ctx (format-rule ctx {column "1234"} line-number)]
            (is (= ctx result-ctx))))))
    (testing "optional column"
      (let [format-rule (create-format-rule filename {:name column :format format/all-digits})]
        (testing "if it's not there, it's okay"
          (let [result-ctx (format-rule ctx {} line-number)]
            (is (= ctx result-ctx))))
        (testing "if it is there"
          (testing "it matches the format, everything's okay"
            (let [result-ctx (format-rule ctx {column "1234"} line-number)]
              (is (= ctx result-ctx))))
          (testing "it doesn't match the format, you get an error"
            (let [result-ctx (format-rule ctx {column "asdf"} line-number)]
              (is (get-in result-ctx [:errors filename line-number column])))))))
    (testing "a check that is a list of options"
      (let [format-rule (create-format-rule filename {:name column :format format/yes-no})]
        (testing "matches"
          (is (= ctx (format-rule ctx {column "yes"} line-number)))
          (is (= ctx (format-rule ctx {column "no"} line-number))))
        (testing "non-matches"
          (is (get-in (format-rule ctx {column "YEP!"} line-number) [:errors filename line-number column]))
          (is (get-in (format-rule ctx {column "no way"} line-number) [:errors filename line-number column])))))
    (testing "a check that is a function"
      (let [palindrome? (fn [v] (= v (clojure.string/reverse v)))
            format-rule (create-format-rule filename {:name column :format {:check palindrome? :message "Not a palindrome"}})]
        (testing "matches"
          (is (= ctx (format-rule ctx {column "able was I ere I saw elba"} line-number)))
          (is (= ctx (format-rule ctx {column "racecar"} line-number))))
        (testing "non-matches"
          (is (get-in (format-rule ctx {column "abcdefg"} line-number) [:errors filename line-number column]))
          (is (get-in (format-rule ctx {column "cleveland"} line-number) [:errors filename line-number column])))))
    (testing "if there's no check function, everything is okay"
      (let [format-rule (create-format-rule filename {:name column})]
        (is (= ctx (format-rule ctx {column "hi"} line-number)))
        (is (= ctx (format-rule ctx {} line-number)))))))

(deftest invalid-utf-8-test
  (testing "marks any value with a Unicode replacement character as invalid UTF-8 because that's what we assume we get"
    (let [ctx (merge {:input (csv-inputs ["invalid-utf8/source.txt"])
                    :data-specs data-specs}
                   (sqlite/temp-db "invalid-utf-8"))
          out-ctx (csv/load-csvs ctx)]
    (testing "reports errors for values with the Unicode replacement character"
      (is (= (get-in out-ctx [:errors "source.txt" 2 "name"])
             "Is not valid UTF-8."))))))
