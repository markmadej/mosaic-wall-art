import unittest
import create_mosaic

class TestIfpModule(unittest.TestCase):

    def test_grid_tuples(self):
        imageSizes = [
            (20, 20),
        ]
        boxSizes = [
            (10, 10),
        ]
        correctGridTuples = [
            [
                (0, 0, 10, 10), (10, 0, 10, 10), (0, 10, 10, 10), (10, 10, 10, 10)
            ],
        ]
        for i in range(0, len(imageSizes)):
            gridTuples = create_mosaic.getGridTuplesFromImageAndBoxDimensions(imageSizes[i], boxSizes[i])
            self.assertEqual(len(correctGridTuples), len(gridTuples))
            for tuple in gridTuples:
                foundMatch = False
                for cTuple in correctGridTuples:
                    if tuple == cTuple:
                        foundMatch = True
                self.assertEqual(foundMatch, True,
                    "Could not find match for tuple {0} in {1}".format(
                        tuple,
                        correctGridTuples[i]
                    )
                )

    def test_next_proposed_box(self):


if __name__ == '__main__':
    unittest.main()
